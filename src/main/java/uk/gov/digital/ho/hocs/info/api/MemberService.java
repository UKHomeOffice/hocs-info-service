package uk.gov.digital.ho.hocs.info.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.digital.ho.hocs.info.domain.exception.ApplicationExceptions;
import uk.gov.digital.ho.hocs.info.domain.model.Member;
import uk.gov.digital.ho.hocs.info.client.ingest.ListConsumerService;
import uk.gov.digital.ho.hocs.info.domain.repository.MemberRepository;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;
    private final ListConsumerService listConsumerService;

    @Autowired
    public MemberService(MemberRepository memberRepository, ListConsumerService listConsumerService) {
        this.memberRepository = memberRepository;
        this.listConsumerService = listConsumerService;
    }

    public Set<Member> getAllActiveMembers() {
        log.debug("Getting all Members");
        Set<Member> members = memberRepository.findAllActiveMembers();
        log.info("Got {} CaseTypes", members.size());
        return members;
    }

    public Member getMember(UUID uuid) {
        log.debug("Requesting House Address for Member {}", uuid);
        Member member = memberRepository.findByUuid(uuid);
        if (member != null) {
            log.info("Got Member {} for UUID {}", member.getFullTitle(), uuid);
            return member;
        } else {
            throw new ApplicationExceptions.EntityNotFoundException("Could not find member %s", uuid);
        }
    }

    public Integer updateWebMemberLists() {
        log.info("Started Updating Members Lists");

        // launch async calls to all members endpoints, threads taken from common pool.
        CompletableFuture<Set<Member>> futureWelsh
                = CompletableFuture.supplyAsync(listConsumerService::createFromWelshAssemblyAPI);
        CompletableFuture<Set<Member>> futureScottish
                = CompletableFuture.supplyAsync(listConsumerService::createFromScottishParliamentAPI);
        CompletableFuture<Set<Member>> futureUKCommons
                = CompletableFuture.supplyAsync(listConsumerService::createCommonsFromUKParliamentAPI);
        CompletableFuture<Set<Member>> futureUKLords
                = CompletableFuture.supplyAsync(listConsumerService::createLordsFromUKParliamentAPI);
        CompletableFuture<Set<Member>> futureIrish
                = CompletableFuture.supplyAsync(listConsumerService::createFromIrishAssemblyAPI);

        Set<Member> membersBulkSet = Stream.of(futureWelsh, futureScottish, futureUKCommons, futureUKLords, futureIrish)
                .map(CompletableFuture::join) // wait for all futures to return
                .collect(HashSet::new, Set::addAll, Set::addAll); // create a new HashSet and flatten future results

        updateMembersBulk(membersBulkSet); // update info members table in single transaction

        log.info("Finished Updating Members Lists");
        return membersBulkSet.size();
    }

    private void updateMembersBulk(Set<Member> members) {

        Set<Member> membersBulk = new HashSet<>();

        members.forEach(member -> {
            Member memberFromDB = memberRepository.findByExternalReference(member.getExternalReference());
            if (memberFromDB != null) {
                memberFromDB.update(member.getFullTitle());
                membersBulk.add(memberFromDB);
            } else {
                membersBulk.add(member);
            }
        });

        memberRepository.saveAll(membersBulk); // save all members with single transaction
        log.debug("{} members processed", membersBulk.size());
    }

}
