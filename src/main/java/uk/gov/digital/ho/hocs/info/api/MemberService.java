package uk.gov.digital.ho.hocs.info.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.digital.ho.hocs.info.domain.exception.ApplicationExceptions;
import uk.gov.digital.ho.hocs.info.domain.model.Constituency;
import uk.gov.digital.ho.hocs.info.domain.model.Member;
import uk.gov.digital.ho.hocs.info.client.ingest.ListConsumerService;
import uk.gov.digital.ho.hocs.info.domain.repository.ConstituencyRepository;
import uk.gov.digital.ho.hocs.info.domain.repository.MemberRepository;

import java.util.Set;
import java.util.UUID;

@Service
@Slf4j
public class MemberService {

    private final ConstituencyRepository constituencyRepository;
    private final MemberRepository memberRepository;
    private final ListConsumerService listConsumerService;

    @Autowired
    public MemberService(ConstituencyRepository constituencyRepository, MemberRepository memberRepository, ListConsumerService listConsumerService) {
        this.constituencyRepository = constituencyRepository;
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
        log.info("Started Updating Constituencies List");
        updateConstituency(listConsumerService.createUKConstituencyFromUKParliamentAPI());
        log.info("Started Updating Members Lists");
        updateMember(listConsumerService.createFromWelshAssemblyAPI());
        updateMember(listConsumerService.createFromScottishParliamentAPI());
        updateMember(listConsumerService.createCommonsFromUKParliamentAPI());
        updateMember(listConsumerService.createLordsFromUKParliamentAPI());
        updateMember(listConsumerService.createFromIrishAssemblyAPI());
        updateMember(listConsumerService.createFromEuropeanParliamentAPI());
        log.info("Finished Updating Members Lists");
    }

    private void updateConstituency(Set<Constituency> constituencySet) {
        constituencySet.forEach(constituency -> {
            log.debug("Looking for constituency by name: {}", constituency.getConstituencyName());
            Constituency constituencyFromDB = constituencyRepository.findActiveConstituencyByName(constituency.getConstituencyName());
            if (constituencyFromDB != null) {
                log.info("Constituency {} found", constituency.getConstituencyName());
            } else {
                log.info("Constituency {} not found, creating", constituency.getConstituencyName());
                constituencyRepository.save(constituency);
            }
        });
    }

    private void updateMember(Set<Member> members) {
        members.forEach(member -> {
            log.debug("Looking for Member: {}", member.getFullTitle());
            Member memberFromDB = memberRepository.findByExternalReference(member.getExternalReference());
            if (memberFromDB != null) {
                log.info("Member {} found, updating", member.getFullTitle());
                memberFromDB.update(member.getFullTitle(), member.getConstituencyUUID(), member.getConstituencyName());
                memberRepository.save(memberFromDB);
            } else {
                log.info("Member {} not found, creating", member.getFullTitle());
                memberRepository.save(member);
            }
        });
    }
}
