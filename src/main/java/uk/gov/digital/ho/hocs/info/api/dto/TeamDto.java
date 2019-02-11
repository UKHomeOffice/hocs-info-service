package uk.gov.digital.ho.hocs.info.api.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import uk.gov.digital.ho.hocs.info.domain.model.Team;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@AllArgsConstructor()
@Getter
@EqualsAndHashCode
public class TeamDto {

    @JsonProperty("displayName")
    private String displayName;

    @JsonProperty("type")
    private UUID uuid;

    @JsonProperty("active")
    private boolean active;

    @JsonProperty("permissions")
    private Set<PermissionDto> permissions;

    @JsonCreator
    public TeamDto(@JsonProperty("displayName") String displayName,@JsonProperty("permissions") Set<PermissionDto> permissions) {
        this.displayName = displayName;
        this.permissions = Optional.ofNullable(permissions).orElse(new HashSet<>());
    }

    public TeamDto(String displayName, UUID uuid, boolean active) {
        this.displayName = displayName;
        this.uuid = uuid;
        this.active = active;
    }

    public static TeamDto from (Team team) {
        return new TeamDto(team.getDisplayName(), team.getUuid(), team.isActive(),
                team.getPermissions().stream().map(permission ->
                        PermissionDto.from(permission)).collect(Collectors.toSet())); }

    //TODO: consider if this can be replaced with a JSONView
    public static TeamDto fromWithoutPermissions(Team team) {
        return new TeamDto(team.getDisplayName(), team.getUuid(), team.isActive());
    }
}
