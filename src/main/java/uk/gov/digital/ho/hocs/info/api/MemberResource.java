package uk.gov.digital.ho.hocs.info.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.digital.ho.hocs.info.api.dto.GetMembersAddressResponse;
import uk.gov.digital.ho.hocs.info.api.dto.GetMembersResponse;
import uk.gov.digital.ho.hocs.info.domain.model.Member;

import java.util.Set;
import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
public class MemberResource {

    private final MemberService memberService;

    @Autowired
    public MemberResource(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping(value = "/member", produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<GetMembersResponse> getAllMembers() {
        Set<Member> members = memberService.getAllActiveMembers();
        return ResponseEntity.ok(GetMembersResponse.from(members));
    }

    @GetMapping(value = "/member/{uuid}", produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<GetMembersAddressResponse> getMember(@PathVariable UUID uuid) {
        Member member = memberService.getMember(uuid);
        return ResponseEntity.ok(GetMembersAddressResponse.from(member));
    }

    @GetMapping(value = "/admin/member/refresh")
    public ResponseEntity getFromApi() {
        Integer rowsProcessed = memberService.updateWebMemberLists();
        return ResponseEntity.ok().body("Rows processed : " + rowsProcessed);
    }

}