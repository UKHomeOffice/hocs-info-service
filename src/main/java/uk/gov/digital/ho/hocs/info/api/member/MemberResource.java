package uk.gov.digital.ho.hocs.info.api.member;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.digital.ho.hocs.info.api.dto.GetMembersAddressResponse;
import uk.gov.digital.ho.hocs.info.api.dto.GetMembersResponse;
import uk.gov.digital.ho.hocs.info.domain.exception.ApplicationExceptions;
import uk.gov.digital.ho.hocs.info.domain.model.Member;

import java.util.Set;
import java.util.UUID;

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
        } catch (ApplicationExceptions.EntityPermissionException e) {
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
        } catch (ApplicationExceptions.IngestException e) {
            log.error("Ingest exception");
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping(value = "/member/{uuid}/address", produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<GetMembersAddressResponse> getMemberAddressByUUID(@PathVariable UUID uuid) {
        log.info("requesting house address for member {}", uuid);
        try {
            Member member = memberService.getMemberAndAddress(uuid);
            return ResponseEntity.ok(GetMembersAddressResponse.from(member));
        } catch (ApplicationExceptions.EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }

    }
}