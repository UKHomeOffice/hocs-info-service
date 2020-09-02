package uk.gov.digital.ho.hocs.info.api;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.keycloak.representations.idm.UserRepresentation;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
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

    UUID userUUID = UUID.randomUUID();

    @Test
    public void shouldGetAllUsers() {
        List<UserRepresentation> userRepresentations = new ArrayList<UserRepresentation>();
        UserRepresentation user = new UserRepresentation();
        user.setId(userUUID.toString());
        user.setFirstName("FirstName");
        user.setFirstName("LastName");
        userRepresentations.add(user);
        when(keycloakService.getAllUsers()).thenReturn(userRepresentations);

        List<UserDto> result = service.getAllUsers();
        assertThat(result.size()).isEqualTo(1);
        verify(keycloakService, times(1)).getAllUsers();
        verifyNoMoreInteractions(keycloakService);

    }

    @Test
    public void shouldGetUserFromUUID() {
        UserRepresentation user = new UserRepresentation();
        user.setId(userUUID.toString());
        user.setFirstName("FirstName");
        user.setLastName("LastName");
        when(keycloakService.getUserFromUUID(userUUID)).thenReturn(user);

        UserDto result = service.getUserByUUID(userUUID);
        assertThat(result.getFirstName()).isEqualTo("FirstName");
        assertThat(result.getLastName()).isEqualTo("LastName");
        assertThat(result.getId()).isEqualTo(userUUID.toString());
        verify(keycloakService, times(1)).getUserFromUUID(userUUID);
        verifyNoMoreInteractions(keycloakService);

    }

    @Test
    public void shouldGetAllUsersForTeam(){
        UUID teamUUID = UUID.randomUUID();
        Unit unit = new Unit("UNIT ONE", "UNIT1", Boolean.TRUE);
        Team team = new Team("Team1",Boolean.TRUE);
        team.setUnit(unit);

        Set<UserRepresentation> userRepresentations = new HashSet<>();
        UserRepresentation user = new UserRepresentation();
        user.setId(userUUID.toString());
        user.setFirstName("FirstName");
        user.setFirstName("LastName");
        userRepresentations.add(user);
        UserRepresentation user2 = new UserRepresentation();
        user2.setId(userUUID.toString());
        user2.setFirstName("FirstName2");
        user2.setFirstName("LastName2");
        userRepresentations.add(user2);

        when(keycloakService.getUsersForTeam(teamUUID)).thenReturn(userRepresentations);

        List<UserDto> result = service.getUsersForTeam(teamUUID);
        assertThat(result.size()).isEqualTo(2);
        verify(keycloakService, times(1)).getUsersForTeam(teamUUID);
        verifyNoMoreInteractions(keycloakService);
    }

    @Test
    public void shouldGetUsersForTeamByStage(){
        UUID caseUUID = UUID.randomUUID();
        UUID stageUUID = UUID.randomUUID();
        UUID teamUUID = UUID.randomUUID();
        String stageType = "some-stage-type";


        Set<UserRepresentation> userRepresentations = new HashSet<>();
        UserRepresentation user = new UserRepresentation();
        user.setId(userUUID.toString());
        user.setFirstName("FirstName");
        user.setFirstName("LastName");
        userRepresentations.add(user);
        UserRepresentation user2 = new UserRepresentation();
        user2.setId(userUUID.toString());
        user2.setFirstName("FirstName2");
        user2.setFirstName("LastName2");
        userRepresentations.add(user2);

        when(keycloakService.getUsersForTeam(teamUUID)).thenReturn(userRepresentations);

        when(caseworkClient.getStageTypeFromStage(caseUUID, stageUUID)).thenReturn(stageType);
        when(stageTypeService.getTeamForStageType(stageType).getUuid()).thenReturn(teamUUID);
        verifyNoMoreInteractions(keycloakService);

        List<UserDto> result = service.getUsersForTeamByStage(caseUUID, stageUUID);
        assertThat(result.size()).isEqualTo(2);
    }

}
