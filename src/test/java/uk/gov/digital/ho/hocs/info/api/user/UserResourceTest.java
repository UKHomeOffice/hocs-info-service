package uk.gov.digital.ho.hocs.info.api.user;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uk.gov.digital.ho.hocs.info.api.dto.UserDto;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        List<UserDto> users = new ArrayList<>();
        UserDto user1 =  new UserDto(user1UUID,"some user","FirstName", "LastName","user1@noemail.com");
        UserDto user2 =  new UserDto(user2UUID,"some user2","FirstName2", "LastName2","user2@noemail.com");
        users.addAll(Stream.of(user1, user2).collect(Collectors.toList()));

        when(userService.getAllUsers()).thenReturn(users);

        userResource = new UserResource(userService);
        ResponseEntity<List<UserDto>> result = userResource.getAllUsers();
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody().size()).isEqualTo(2);
    }

    @Test
    public void shouldGetUserByUUID() {

        UUID userUUID = UUID.randomUUID();
        UserDto user =  new UserDto(userUUID.toString(),"some user","FirstName", "LastName","user1@noemail.com");

        when(userService.getUserByUUID(userUUID)).thenReturn(user);

        userResource = new UserResource(userService);
        ResponseEntity<UserDto> result = userResource.getUserByUUID(userUUID);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void shouldGetAllUsersForTeam() {
        String user1UUID = UUID.randomUUID().toString();
        String user2UUID = UUID.randomUUID().toString();
        String teamUUID = UUID.randomUUID().toString();

        List<UserDto> users = new ArrayList<>();
        UserDto user1 =  new UserDto(user1UUID,"some user","FirstName", "LastName","user1@noemail.com");
        UserDto user2 =  new UserDto(user2UUID,"some user2","FirstName2", "LastName2","user2@noemail.com");
        users.addAll(Stream.of(user1, user2).collect(Collectors.toList()));


        when(userService.getUsersForTeam(teamUUID)).thenReturn(users);

        userResource = new UserResource(userService);
        ResponseEntity<List<UserDto>> result = userResource.getUsersForTeam(teamUUID);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody().size()).isEqualTo(2);
    }
}