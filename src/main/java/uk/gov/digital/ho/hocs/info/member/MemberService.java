package uk.gov.digital.ho.hocs.info.member;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.digital.ho.hocs.info.RequestData;
import uk.gov.digital.ho.hocs.info.casetype.CaseTypeService;
import uk.gov.digital.ho.hocs.info.entities.Member;
import uk.gov.digital.ho.hocs.info.exception.EntityPermissionException;
import uk.gov.digital.ho.hocs.info.repositories.MemberRepository;

import java.util.Set;

@Service
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;
    private final CaseTypeService caseTypeService;
    private final RequestData requestData;

    @Autowired
    public MemberService(MemberRepository memberRepository, CaseTypeService caseTypeService, RequestData requestData) {
        this.memberRepository = memberRepository;
        this.caseTypeService = caseTypeService;
        this.requestData = requestData;

    }

    public Set<Member> getActiveMembersByCaseType(String caseType) throws EntityPermissionException {
        log.debug("Requesting all Members for CaseType {}", caseType);
        if (caseTypeService.hasPermissionForCaseType(caseType)) {
            return memberRepository.findAllActiveMembersForCaseType(caseType);
        } else {
            throw new EntityPermissionException("Not allowed to get Members for CaseType, CaseType: %s not in Roles: %s", caseType, requestData.rolesString());
        }
    }
}
