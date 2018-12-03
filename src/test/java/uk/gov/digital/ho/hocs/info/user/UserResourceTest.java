package uk.gov.digital.ho.hocs.info.user;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.keycloak.representations.idm.UserRepresentation;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uk.gov.digital.ho.hocs.info.dto.UserDto;
import uk.gov.digital.ho.hocs.info.security.KeycloakService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserResourceTest {

    @Mock UserService userService;

    UserResource userResource;

    @Test
    public void shouldGetAllUsers() {

        String user1UUID = UUID.randomUUID().toString();
        String user2UUID = UUID.randomUUID().toString();
        List<UserRepresentation> userRepresentations = new ArrayList<>();
        UserRepresentation user1 =  new UserRepresentation();
        user1.setId(user1UUID);
        user1.setFirstName("FirstName1");
        user1.setFirstName("LastName1");
        user1.setUsername("user1@noemail.com");
        userRepresentations.add(user1);

        UserRepresentation user2 =  new UserRepresentation();
        user2.setId(user2UUID);
        user2.setFirstName("FirstName2");
        user2.setFirstName("LastName2");
        user2.setUsername("user2@noemail.com");
        userRepresentations.add(user2);

        when(userService.getAllUsers()).thenReturn(userRepresentations);

        userResource = new UserResource(userService);
        ResponseEntity<List<UserDto>> result = userResource.getAllUsers();
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody().size()).isEqualTo(2);
    }

    @Test
    public void shouldGetAllUsersForTeam() {
        String user1UUID = UUID.randomUUID().toString();
        String user2UUID = UUID.randomUUID().toString();
        String teamUUID = UUID.randomUUID().toString();
        List<UserRepresentation> userRepresentations = new ArrayList<>();
        UserRepresentation user1 =  new UserRepresentation();
        user1.setId(user1UUID);
        user1.setFirstName("FirstName1");
        user1.setFirstName("LastName1");
        user1.setUsername("user1@noemail.com");
        userRepresentations.add(user1);

        UserRepresentation user2 =  new UserRepresentation();
        user2.setId(user2UUID);
        user2.setFirstName("FirstName2");
        user2.setFirstName("LastName2");
        user2.setUsername("user2@noemail.com");
        userRepresentations.add(user2);

        when(userService.getUsersForTeam(teamUUID)).thenReturn(userRepresentations);

        userResource = new UserResource(userService);
        ResponseEntity<List<UserDto>> result = userResource.getUsersForTeam(teamUUID);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody().size()).isEqualTo(2);
    }
}