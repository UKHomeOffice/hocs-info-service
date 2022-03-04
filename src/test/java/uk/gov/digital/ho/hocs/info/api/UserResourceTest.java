package uk.gov.digital.ho.hocs.info.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uk.gov.digital.ho.hocs.info.api.dto.CreateUserDto;
import uk.gov.digital.ho.hocs.info.api.dto.CreateUserResponse;
import uk.gov.digital.ho.hocs.info.api.dto.UpdateUserDto;
import uk.gov.digital.ho.hocs.info.api.dto.UserDto;

@RunWith(MockitoJUnitRunner.class)
public class UserResourceTest {

    @Mock
    UserService userService;

    UserResource userResource;

    @Before
    public void setUp() {
        userResource = new UserResource(userService);
    }

    @Test
    public void shouldGetAllUsers() {

        String user1UUID = UUID.randomUUID().toString();
        String user2UUID = UUID.randomUUID().toString();
        List<UserDto> users = new ArrayList<>();
        UserDto user1 =  new UserDto(user1UUID,"some user", "user1@noemail.com","FirstName", "LastName", true);
        UserDto user2 =  new UserDto(user2UUID,"some user2", "user2@noemail.com","FirstName2", "LastName2", true);
        users.addAll(Stream.of(user1, user2).collect(Collectors.toList()));

        when(userService.getAllUsers()).thenReturn(users);

        ResponseEntity<List<UserDto>> result = userResource.getAllUsers();
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody().size()).isEqualTo(2);
    }

    @Test
    public void shouldGetUserByUUID() {

        UUID userUUID = UUID.randomUUID();
        UserDto user =  new UserDto(userUUID.toString(),"some user", "user1@noemail.com","FirstName", "LastName", true);

        when(userService.getUserByUUID(userUUID)).thenReturn(user);

        ResponseEntity<UserDto> result = userResource.getUserByUUID(userUUID);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void shouldGetAllUsersForTeam() {
        String user1UUID = UUID.randomUUID().toString();
        String user2UUID = UUID.randomUUID().toString();
        UUID teamUUID = UUID.randomUUID();

        List<UserDto> users = new ArrayList<>();
        UserDto user1 =  new UserDto(user1UUID,"some user", "user1@noemail.com","FirstName", "LastName", true);
        UserDto user2 =  new UserDto(user2UUID,"some user2", "user2@noemail.com","FirstName2", "LastName2", true);
        users.addAll(Stream.of(user1, user2).collect(Collectors.toList()));


        when(userService.getUsersForTeam(teamUUID)).thenReturn(users);

        ResponseEntity<List<UserDto>> result = userResource.getUsersForTeam(teamUUID);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody().size()).isEqualTo(2);
    }

    @Test
    public void shouldGetUserForTeam() {
        UUID teamUUID = UUID.randomUUID();
        UUID userUUID = UUID.randomUUID();
        UserDto user =  new UserDto(userUUID.toString(),"some user", "user1@noemail.com","FirstName", "LastName", true);
        when(userService.getUserIfInTeam(userUUID, teamUUID)).thenReturn(user);

        ResponseEntity<UserDto> result = userResource.getUserForTeam(teamUUID, userUUID);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isEqualTo(user);
    }

    @Test
    public void shouldCreateUser() {

        //given
        CreateUserDto createUserDto = new CreateUserDto();
        CreateUserResponse createUserResponse = new CreateUserResponse();
        when(userService.createUser(createUserDto)).thenReturn(createUserResponse);

        //when
        ResponseEntity<CreateUserResponse> response = userResource.createUser(createUserDto);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(createUserResponse);
    }

    @Test
    public void shouldUpdateUser() {

        //given
        UUID userUUID = UUID.randomUUID();
        UpdateUserDto updateUserDto = new UpdateUserDto();

        //when
        ResponseEntity response = userResource.updateUser(userUUID, updateUserDto);

        //then
        verify(userService).updateUser(userUUID, updateUserDto);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
