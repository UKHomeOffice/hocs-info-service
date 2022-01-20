package uk.gov.digital.ho.hocs.info.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UserWithTeamsDto {
    private String id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private List<String> teamNames;
<<<<<<< HEAD
    private Map<String, String> unitNames;
    private boolean enabled;

    public static UserWithTeamsDto from(UserDto user, List<String> teamNames, Map<String, String> unitNames) {
=======
    private List<String> unitNames;
    private boolean enabled;

    public static UserWithTeamsDto from(UserDto user, List<String> teamNames, List<String> unitNames) {
>>>>>>> 44c4cae (HOCS-4450 added units to DTO and changed teams to pass through names)
        return new UserWithTeamsDto(user.getId(), user.getUsername(), user.getEmail(), user.getFirstName(), user.getLastName(), teamNames, unitNames, user.isEnabled());
    }
}
