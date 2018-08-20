package uk.gov.digital.ho.hocs.info.member;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.gov.digital.ho.hocs.info.casetype.CaseTypeService;
import uk.gov.digital.ho.hocs.info.dto.GetMembersResponse;
import uk.gov.digital.ho.hocs.info.entities.Member;
import uk.gov.digital.ho.hocs.info.exception.EntityPermissionException;

import java.util.Set;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@Slf4j
@RestController
public class MemberResource {

    private final MemberService memberService;
    private final CaseTypeService caseTypeService;


    @Autowired
    public MemberResource(MemberService memberService, CaseTypeService caseTypeService) {
        this.memberService = memberService;
        this.caseTypeService = caseTypeService;
    }

    @RequestMapping(value = "/casetype/{caseType}/members", method = RequestMethod.GET, produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<GetMembersResponse> getAllMembers(@PathVariable String caseType) {
        try {
            Set<Member> members = memberService.getActiveMembersByCaseType(caseType);
            return ResponseEntity.ok(GetMembersResponse.from(members));
        } catch ( EntityPermissionException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build() ;
        }
    }
}
