package uk.gov.digital.ho.hocs.info.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CreateUserDto {

    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    boolean temporaryPassword;
    private List<String> realmRoles;
    private List<UUID> teams;


}



