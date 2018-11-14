package uk.gov.digital.ho.hocs.info.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.digital.ho.hocs.info.entities.Team;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@AllArgsConstructor()
@Getter
public class TeamDto {

    @JsonProperty("displayName")
    private String displayName;

    @JsonProperty("type")
    private UUID uuid;

    @JsonProperty("permissions")
    private Set<PermissionDto> permissions;

    @JsonCreator
    public TeamDto(@JsonProperty("displayName") String displayName,@JsonProperty("permissions") Set<PermissionDto> permissions) {
        this.displayName = displayName;
        this.uuid = UUID.randomUUID();
        this.permissions = Optional.ofNullable(permissions).orElse(new HashSet<>());
    }

    public static TeamDto from (Team team) {
        return new TeamDto(team.getDisplayName(), team.getUuid(),
                team.getPermissions().stream().map(permission ->
                        PermissionDto.from(permission)).collect(Collectors.toSet())); }
}
