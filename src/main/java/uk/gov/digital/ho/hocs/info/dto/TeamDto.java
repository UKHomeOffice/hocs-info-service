package uk.gov.digital.ho.hocs.info.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.digital.ho.hocs.info.entities.Team;
import uk.gov.digital.ho.hocs.info.entities.Unit;

import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class TeamDto {

    @JsonProperty("displayName")
    private String displayName;

    @JsonProperty("type")
    private String uuid;

    public static TeamDto from (Team team) {
        return new TeamDto(team.getDisplayName(), team.getUuid()); }
}
