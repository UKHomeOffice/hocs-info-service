package uk.gov.digital.ho.hocs.info.api;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.keycloak.representations.idm.UserRepresentation;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.digital.ho.hocs.info.api.dto.CreateUserDto;
import uk.gov.digital.ho.hocs.info.api.dto.CreateUserResponse;
import uk.gov.digital.ho.hocs.info.api.dto.UpdateUserDto;
import uk.gov.digital.ho.hocs.info.api.dto.UserDto;
import uk.gov.digital.ho.hocs.info.client.caseworkclient.CaseworkClient;
import uk.gov.digital.ho.hocs.info.domain.model.Team;
import uk.gov.digital.ho.hocs.info.domain.model.Unit;
import uk.gov.digital.ho.hocs.info.security.KeycloakService;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    private UserService service;

    @Mock
    private KeycloakService keycloakService;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private StageTypeService stageTypeService;

    @Mock
    private CaseworkClient caseworkClient;

    @Before
    public void setUp() {
        this.service = new UserService(keycloakService, caseworkClient, stageTypeService);
    }

    @Test
    public void shouldGetAllUsers() {
        List<UserRepresentation> userRepresentations = new ArrayList<UserRepresentation>();
        UserRepresentation user = generateUserRepresentation(1);
        userRepresentations.add(user);
        when(keycloakService.getAllUsers()).thenReturn(userRepresentations);

        List<UserDto> result = service.getAllUsers();
        assertThat(result.size()).isEqualTo(1);
        verify(keycloakService, times(1)).getAllUsers();
        verifyNoMoreInteractions(keycloakService);

    }

    @Test
    public void shouldGetUserFromUUID() {
        UserRepresentation user = generateUserRepresentation(1);
        UUID userUUID = UUID.fromString(user.getId());
        when(keycloakService.getUserFromUUID(userUUID)).thenReturn(user);

        UserDto result = service.getUserByUUID(userUUID);
        assertThat(result.getUsername()).isEqualTo(user.getUsername());
        assertThat(result.getEmail()).isEqualTo(user.getEmail());
        assertThat(result.getFirstName()).isEqualTo(user.getFirstName());
        assertThat(result.getLastName()).isEqualTo(user.getLastName());
        assertThat(result.isEnabled()).isEqualTo(user.isEnabled());
        assertThat(result.getId()).isEqualTo(userUUID.toString());
        verify(keycloakService, times(1)).getUserFromUUID(userUUID);
        verifyNoMoreInteractions(keycloakService);

    }

    @Test
    public void shouldGetAllUsersForTeam() {
        UUID teamUUID = UUID.randomUUID();
        Unit unit = new Unit("UNIT ONE", "UNIT1", Boolean.TRUE);
        Team team = new Team("Team1", Boolean.TRUE);
        team.setUnit(unit);

        List<UserRepresentation> userRepresentations = new ArrayList<>();
        UserRepresentation user = generateUserRepresentation(1);
        userRepresentations.add(user);
        UserRepresentation user2 = generateUserRepresentation(2);
        userRepresentations.add(user2);

        when(keycloakService.getUsersForTeam(teamUUID)).thenReturn(userRepresentations);

        List<UserDto> result = service.getUsersForTeam(teamUUID);
        assertThat(result.size()).isEqualTo(2);
        verify(keycloakService, times(1)).getUsersForTeam(teamUUID);
        verifyNoMoreInteractions(keycloakService);
    }

    @Test
    public void shouldGetUserForTeam_whenUserIsPresent() {
        UUID teamUUID = UUID.randomUUID();
        Unit unit = new Unit("UNIT ONE", "UNIT1", Boolean.TRUE);
        Team team = new Team("Team1", Boolean.TRUE);
        team.setUnit(unit);
        List<UserRepresentation> userRepresentations = new ArrayList<>();
        UserRepresentation user1 = generateUserRepresentation(1);
        userRepresentations.add(user1);
        UserRepresentation user2 = generateUserRepresentation(2);
        UUID userUUID2 = UUID.fromString(user2.getId());
        userRepresentations.add(user2);
        when(keycloakService.getUsersForTeam(teamUUID)).thenReturn(userRepresentations);

        UserDto result = service.getUserForTeam(teamUUID, userUUID2);

        assertThat(result.getId()).isEqualTo(userUUID2.toString());
        assertThat(result.getFirstName()).isEqualTo(user2.getFirstName());
        assertThat(result.getLastName()).isEqualTo(user2.getLastName());
        verify(keycloakService, times(1)).getUsersForTeam(teamUUID);
        verifyNoMoreInteractions(keycloakService);
    }

    @Test
    public void shouldGetUserForTeam_whenUserIsNotPresent() {
        UUID teamUUID = UUID.randomUUID();
        Unit unit = new Unit("UNIT ONE", "UNIT1", Boolean.TRUE);
        Team team = new Team("Team1", Boolean.TRUE);
        team.setUnit(unit);
        List<UserRepresentation> userRepresentations = new ArrayList<>();
        UserRepresentation user1 = generateUserRepresentation(1);
        userRepresentations.add(user1);
        UserRepresentation user2 = generateUserRepresentation(2);
        userRepresentations.add(user2);
        when(keycloakService.getUsersForTeam(teamUUID)).thenReturn(userRepresentations);

        UserDto result = service.getUserForTeam(teamUUID, UUID.randomUUID());

        assertThat(result).isNull();
        verify(keycloakService, times(1)).getUsersForTeam(teamUUID);
        verifyNoMoreInteractions(keycloakService);
    }

    @Test
    public void shouldGetUsersForTeamByStage() {
        UUID caseUUID = UUID.randomUUID();
        UUID stageUUID = UUID.randomUUID();
        UUID teamUUID = UUID.randomUUID();
        String stageType = "some-stage-type";

        List<UserRepresentation> userRepresentations = new ArrayList<>();
        UserRepresentation user1 = generateUserRepresentation(1);
        userRepresentations.add(user1);
        UserRepresentation user2 = generateUserRepresentation(2);
        userRepresentations.add(user2);

        when(keycloakService.getUsersForTeam(teamUUID)).thenReturn(userRepresentations);

        when(caseworkClient.getStageTypeFromStage(caseUUID, stageUUID)).thenReturn(stageType);
        when(stageTypeService.getTeamForStageType(stageType).getUuid()).thenReturn(teamUUID);
        verifyNoMoreInteractions(keycloakService);

        List<UserDto> result = service.getUsersForTeamByStage(caseUUID, stageUUID);
        assertThat(result.size()).isEqualTo(2);
    }

    @Test
    public void shouldCreateUser() {

        //given
        CreateUserDto createUserDto = new CreateUserDto();
        CreateUserResponse createUserResponse = new CreateUserResponse();
        when(keycloakService.createUser(createUserDto)).thenReturn(createUserResponse);

        //when
        CreateUserResponse response = service.createUser(createUserDto);

        //then
        assertThat(response).isEqualTo(createUserResponse);
    }

    @Test
    public void shouldUpdateUser() {

        //given
        UUID userUUID = UUID.randomUUID();
        UpdateUserDto updateUserDto = new UpdateUserDto();

        //when
        service.updateUser(userUUID, updateUserDto);

        //then
        verify(keycloakService).updateUser(userUUID, updateUserDto);
    }

    private UserRepresentation generateUserRepresentation(int index) {
        UserRepresentation user = new UserRepresentation();
        user.setId(UUID.randomUUID().toString());
        user.setUsername("username" + index);
        user.setEmail("email" + index);
        user.setFirstName("FirstName" + index);
        user.setLastName("LastName" + index);
        user.setEnabled(true);
        return user;
    }

}
