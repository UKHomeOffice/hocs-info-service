package uk.gov.digital.ho.hocs.info.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.digital.ho.hocs.info.entities.Member;

import java.util.List;
import java.util.Set;

@AllArgsConstructor(access =  AccessLevel.PRIVATE)
@Getter
public class GetMembersResponse {

    @JsonProperty("members")
    Set<Member> members;

    public static GetMembersResponse from(Set<Member> members) {
        return new GetMembersResponse(members);
    }

}
