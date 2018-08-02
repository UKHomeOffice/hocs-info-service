package uk.gov.digital.ho.hocs.info.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.digital.ho.hocs.info.entities.Team;
import uk.gov.digital.ho.hocs.info.entities.Unit;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class UnitDto {

    @JsonProperty("displayName")
    private String displayName;

    @JsonProperty("type")
    private String uuid;

    @JsonProperty("teams")
    private Set<TeamDto> teams = new HashSet<>();

    public static UnitDto from (Unit unit) {
        Set<TeamDto> teamDtos = unit.getTeams().stream().map(TeamDto::from).collect(Collectors.toSet());

        return new UnitDto(unit.getDisplayName(), unit.getUuid(), teamDtos); }
}
