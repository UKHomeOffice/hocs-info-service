package uk.gov.digital.ho.hocs.info.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import uk.gov.digital.ho.hocs.info.api.dto.CreateUserDto;
import uk.gov.digital.ho.hocs.info.api.dto.CreateUserResponse;
import uk.gov.digital.ho.hocs.info.api.dto.UpdateUserDto;
import uk.gov.digital.ho.hocs.info.api.dto.UserDto;
import uk.gov.digital.ho.hocs.info.application.RequestData;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserResourceTest {

    @Mock
    UserService userService;

    UserResource userResource;

    @Before
    public void setUp() {
        userResource = new UserResource(userService, new ObjectMapper());
    }

    @Test
    public void shouldGetAllUsers() throws Exception {

        String user1UUID = UUID.randomUUID().toString();
        String user2UUID = UUID.randomUUID().toString();
        UserDto user1 = new UserDto(user1UUID, "some user", "user1@noemail.com", "FirstName", "LastName", true);
        UserDto user2 = new UserDto(user2UUID, "some user2", "user2@noemail.com", "FirstName2", "LastName2", true);
        List<UserDto> users = Stream.of(user1, user2).toList();

        when(userService.streamAllUsers()).thenReturn(users.stream());

        ResponseEntity<StreamingResponseBody> result = userResource.getAllUsers();
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        assertThat(result.getBody()).isNotNull();
        result.getBody().writeTo(outputStream);

        assertThat(outputStream.toString()).contains(user1UUID, user2UUID);
        verify(userService).streamAllUsers();
    }

    @Test
    public void shouldPropagateCorrelationIdForStreamingUsers() throws Exception {
        String correlationId = "corr-123";
        String userUUID = UUID.randomUUID().toString();
        UserDto user = new UserDto(userUUID, "some user", "user@noemail.com", "FirstName", "LastName", true);

        when(userService.streamAllUsers()).thenAnswer(invocation -> {
            assertThat(MDC.get(RequestData.CORRELATION_ID_HEADER)).isEqualTo(correlationId);
            return Stream.of(user);
        });

        MDC.put(RequestData.CORRELATION_ID_HEADER, correlationId);
        ResponseEntity<StreamingResponseBody> result = userResource.getAllUsers();
        MDC.clear();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        assertThat(result.getBody()).isNotNull();
        result.getBody().writeTo(outputStream);

        assertThat(outputStream.toString()).contains(userUUID);
        verify(userService).streamAllUsers();
        MDC.clear();
    }

    @Test
    public void shouldGetUserByUUID() {

        UUID userUUID = UUID.randomUUID();
        UserDto user = new UserDto(userUUID.toString(), "some user", "user1@noemail.com", "FirstName", "LastName",
            true);

        when(userService.getUserByUUID(userUUID)).thenReturn(user);

        ResponseEntity<UserDto> result = userResource.getUserByUUID(userUUID);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void shouldGetAllUsersForTeam() {
        String user1UUID = UUID.randomUUID().toString();
        String user2UUID = UUID.randomUUID().toString();
        UUID teamUUID = UUID.randomUUID();

        UserDto user1 = new UserDto(user1UUID, "some user", "user1@noemail.com", "FirstName", "LastName", true);
        UserDto user2 = new UserDto(user2UUID, "some user2", "user2@noemail.com", "FirstName2", "LastName2", true);
        List<UserDto> users = List.of(user1, user2);

        when(userService.getUsersForTeam(teamUUID)).thenReturn(users);

        ResponseEntity<List<UserDto>> result = userResource.getUsersForTeam(teamUUID);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().size()).isEqualTo(2);
    }

    @Test
    public void shouldGetUserForTeam() {
        UUID teamUUID = UUID.randomUUID();
        UUID userUUID = UUID.randomUUID();
        UserDto user = new UserDto(userUUID.toString(), "some user", "user1@noemail.com", "FirstName", "LastName",
            true);
        when(userService.getUserForTeam(teamUUID, userUUID)).thenReturn(user);

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
        ResponseEntity<Void> response = userResource.updateUser(userUUID, updateUserDto);

        //then
        verify(userService).updateUser(userUUID, updateUserDto);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

}
