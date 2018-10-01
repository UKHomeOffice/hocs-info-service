package uk.gov.digital.ho.hocs.info.member;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.digital.ho.hocs.info.dto.GetMembersResponse;
import uk.gov.digital.ho.hocs.info.entities.Member;
import uk.gov.digital.ho.hocs.info.exception.EntityPermissionException;
import uk.gov.digital.ho.hocs.info.exception.IngestException;

import java.util.Set;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@Slf4j
@RestController
public class MemberResource {

    private final MemberService memberService;

    @Autowired
    public MemberResource(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping(value = "/casetype/{caseType}/allmembers", produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<GetMembersResponse> getAllActiveMembers(@PathVariable String caseType) {
        try {
            Set<Member> members = memberService.getAllActiveMembers(caseType);
            return ResponseEntity.ok(GetMembersResponse.from(members));
        } catch (EntityPermissionException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @GetMapping(value = "/members/refresh")
    public ResponseEntity getFromApi() {
        log.info("Updating Members");
        try {
            memberService.updateWebMemberLists();
            log.info("Members Updated");
            return ResponseEntity.ok().build();
        } catch (IngestException e) {
            log.error("Ingest exception");
            return ResponseEntity.badRequest().build();
        }
    }
}