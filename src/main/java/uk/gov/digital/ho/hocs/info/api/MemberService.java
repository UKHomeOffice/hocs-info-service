package uk.gov.digital.ho.hocs.info.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.digital.ho.hocs.info.domain.exception.ApplicationExceptions;
import uk.gov.digital.ho.hocs.info.domain.model.Member;
import uk.gov.digital.ho.hocs.info.client.ingest.ListConsumerService;
import uk.gov.digital.ho.hocs.info.domain.repository.MemberRepository;

import java.util.Set;
import java.util.UUID;

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

    Set<Member> getAllActiveMembers() {
        log.debug("Getting all Members");
        Set<Member> members = memberRepository.findAllActiveMembers();
        log.info("Got {} CaseTypes", members.size());
        return members;
    }

    Member getMember(UUID uuid) {
        log.debug("Requesting House Address for Member {}", uuid);
        Member member = memberRepository.findByUuid(uuid);
        if (member != null) {
            log.info("Got Member {} for UUID {}", member.getFullTitle(), uuid);
            return member;
        } else {
            throw new ApplicationExceptions.EntityNotFoundException("Could not find member %s", uuid);
        }
    }

    void updateWebMemberLists() {
        log.info("Started Updating Members Lists");
        updateMember(listConsumerService.createFromWelshAssemblyAPI());
        updateMember(listConsumerService.createFromScottishParliamentAPI());
        updateMember(listConsumerService.createCommonsFromUKParliamentAPI());
        updateMember(listConsumerService.createLordsFromUKParliamentAPI());
        updateMember(listConsumerService.createFromIrishAssemblyAPI());
        log.info("Finished Updating Members Lists");
    }

    private void updateMember(Set<Member> members) {
        members.forEach(member -> {
            log.debug("Looking for Member: {}", member.getFullTitle());
            Member memberFromDB = memberRepository.findByExternalReference(member.getExternalReference());
            if (memberFromDB != null) {
                log.info("Member {} found, updating", member.getFullTitle());
                memberFromDB.update(member.getFullTitle());
                memberRepository.save(memberFromDB);
            } else {
                log.info("Member {} not found, creating", member.getFullTitle());
                memberRepository.save(member);
            }
        });
    }
}
