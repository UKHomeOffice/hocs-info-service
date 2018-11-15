package uk.gov.digital.ho.hocs.info.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import uk.gov.digital.ho.hocs.info.entities.Unit;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@AllArgsConstructor()
@Getter
@EqualsAndHashCode
public class UnitDto {

    @JsonProperty("displayName")
    private String displayName;

    @JsonProperty("type")
    private String uuid;

    @JsonProperty("teams")
    private Set<TeamDto> teams;

    @JsonProperty("shortCode")
    private String shortCode;

    @JsonCreator
    public UnitDto(@JsonProperty("displayName") String displayName, @JsonProperty("shortCode") String shortCode) {
        this.displayName = displayName;
        this.uuid = UUID.randomUUID().toString();
        this.shortCode = shortCode;
    }

    public UnitDto(String displayName, String uuid, String shortCode) {
        this.displayName = displayName;
        this.uuid = uuid;
        this.shortCode = shortCode;
    }

    //TODO: consider if this can be replaced with a JSONView
    public static UnitDto fromWithoutTeams(Unit unit) {
        return new UnitDto(unit.getDisplayName(), unit.getUuid().toString(), unit.getShortCode());
    }

    public static UnitDto from(Unit unit) {
        Set<TeamDto> teamDtos = unit.getTeams().stream().map(TeamDto::from).collect(Collectors.toSet());
        return new UnitDto(unit.getDisplayName(), unit.getUuid().toString(), teamDtos, unit.getShortCode());
    }
}
