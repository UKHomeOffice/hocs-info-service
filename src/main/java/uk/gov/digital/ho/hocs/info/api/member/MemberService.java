package uk.gov.digital.ho.hocs.info.api.member;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.digital.ho.hocs.info.application.RequestData;
import uk.gov.digital.ho.hocs.info.api.casetype.CaseTypeService;
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
    private final CaseTypeService caseTypeService;
    private final ListConsumerService listConsumerService;
    private final RequestData requestData;

    @Autowired
    public MemberService(MemberRepository memberRepository, CaseTypeService caseTypeService, ListConsumerService listConsumerService, RequestData requestData) {
        this.memberRepository = memberRepository;
        this.caseTypeService = caseTypeService;
        this.listConsumerService = listConsumerService;
        this.requestData = requestData;
    }

    public Member getMemberAndAddress(UUID uuid) {
        log.debug("Requesting House Address for Member {}", uuid);
        Member member = memberRepository.findByUuid(uuid);
        if (member != null) {
            return member;
        } else {
            throw new ApplicationExceptions.EntityNotFoundException("Could not find member %s", uuid);
        }
    }

    public Set<Member> getAllActiveMembers(String caseType) {
        log.debug("Requesting all Members");
            return memberRepository.findAllActiveMembers();
    }

    public void updateWebMemberLists() throws ApplicationExceptions.IngestException {
        updateMember(listConsumerService.createFromWelshAssemblyAPI());
        updateMember(listConsumerService.createFromScottishParliamentAPI());
        updateMember(listConsumerService.createCommonsFromUKParliamentAPI());
        updateMember(listConsumerService.createLordsFromUKParliamentAPI());
        updateMember(listConsumerService.createFromIrishAssemblyAPI());
        updateMember(listConsumerService.createFromEuropeanParliamentAPI());
    }

    private void updateMember(Set<Member> members) {
        members.forEach(member -> {
            Member memberFromDB = memberRepository.findByExternalReference(member.getExternalReference());
            if (memberFromDB != null) {
                memberFromDB.update(member.getFullTitle());
                memberRepository.save(memberFromDB);
            } else {
                memberRepository.save(member);
            }
        });
    }
}
