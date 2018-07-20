package uk.gov.digital.ho.hocs.info.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import uk.gov.digital.ho.hocs.info.entities.Member;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class GetMembersResponse {

    @JsonProperty("members")
    List<Member> members;

    public static GetMembersResponse from(final List<Member> members) {
        return new GetMembersResponse(members);
    }

}
