package uk.gov.digital.ho.hocs.info.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.digital.ho.hocs.info.entities.Member;


@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class MemberDto {

    @JsonProperty("label")
    private String displayName;

    @JsonProperty("value")
    private String uuid;

    @JsonProperty("group")
    private String house;


    public static MemberDto from(Member member) {
        return new MemberDto(member.getFullTitle(), member.getUuid(), member.getHouseId().toString());
    }
}