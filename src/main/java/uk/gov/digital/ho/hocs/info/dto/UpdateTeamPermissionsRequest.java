package uk.gov.digital.ho.hocs.info.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Getter
@EqualsAndHashCode
public class UpdateTeamPermissionsRequest {

        @JsonProperty("permissions")
        private Set<PermissionDto> permissions;

        @JsonCreator
        public UpdateTeamPermissionsRequest(@JsonProperty("permissions") Set<PermissionDto> permissions) {
                this.permissions = Optional.ofNullable(permissions).orElse(new HashSet<>());
        }
}
