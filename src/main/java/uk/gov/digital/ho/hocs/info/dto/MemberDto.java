package uk.gov.digital.ho.hocs.info.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.digital.ho.hocs.info.entities.Member;


@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class MemberDto {

    @JsonProperty("displayName")
    private String displayName;

    @JsonProperty("type")
    private String uuid;

    public static MemberDto from (Member member) {
        return new MemberDto(member.getDisplayName(), member.getUuid());
    }
}