package uk.gov.digital.ho.hocs.info.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.keycloak.representations.idm.UserRepresentation;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UserDto {

    private String id;

    private String username;

    private String email;

    private String firstName;

    private String lastName;

    private boolean enabled;

    public static UserDto from(UserRepresentation user) {
        return new UserDto(user.getId(), user.getUsername(), user.getEmail(), user.getFirstName(), user.getLastName(),
            user.isEnabled());
    }

}



