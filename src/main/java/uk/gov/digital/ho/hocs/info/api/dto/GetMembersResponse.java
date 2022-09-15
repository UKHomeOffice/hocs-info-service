package uk.gov.digital.ho.hocs.info.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.digital.ho.hocs.info.domain.model.Member;

import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class GetMembersResponse {

    @JsonProperty("members")
    Set<MemberDto> members;

    public static GetMembersResponse from(Set<Member> members) {
        Set<MemberDto> memberDtos = members.stream().map(MemberDto::from).collect(Collectors.toSet());
        return new GetMembersResponse(memberDtos);
    }

}
