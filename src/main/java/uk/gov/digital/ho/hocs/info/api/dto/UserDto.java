package uk.gov.digital.ho.hocs.info.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.keycloak.representations.idm.UserRepresentation;

@AllArgsConstructor
@Getter
public class UserDto {

    private String id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;

    public static UserDto from(UserRepresentation user) {
        return new UserDto(user.getId(),user.getUsername(), user.getFirstName(),
                user.getLastName(),user.getEmail());
    }


}



