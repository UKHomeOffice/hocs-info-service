package uk.gov.digital.ho.hocs.info.user;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.keycloak.representations.idm.UserRepresentation;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.digital.ho.hocs.info.dto.UserDto;
import uk.gov.digital.ho.hocs.info.entities.Team;
import uk.gov.digital.ho.hocs.info.entities.Unit;
import uk.gov.digital.ho.hocs.info.exception.EntityNotFoundException;
import uk.gov.digital.ho.hocs.info.repositories.TeamRepository;
import uk.gov.digital.ho.hocs.info.security.KeycloakService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    private UserService service;

    @Mock
    private KeycloakService keycloakService;

    @Mock
    private TeamRepository teamRepository;

    @Before
    public void setUp() {
        this.service = new UserService(keycloakService, teamRepository);
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
    public void shouldGetAllUsersForTeam(){
        UUID teamUUID = UUID.randomUUID();
        Unit unit = new Unit("UNIT ONE", "UNIT1", UUID.randomUUID(), Boolean.TRUE);
        Team team = new Team(1l, "Team1",teamUUID,Boolean.TRUE, unit, new HashSet<>());
        when(teamRepository.findByUuid(teamUUID)).thenReturn(team);

        List<UserRepresentation> userRepresentations = new ArrayList<UserRepresentation>();
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

        String path = String.format("/%s/%s",team.getUnit().getShortCode(),teamUUID);
        when(keycloakService.getUsersForTeam(path,teamUUID.toString())).thenReturn(userRepresentations);

        List<UserDto> result = service.getUsersForTeam(teamUUID.toString());
        assertThat(result.size()).isEqualTo(2);
        verify(keycloakService, times(1)).getUsersForTeam(path,teamUUID.toString());
        verifyNoMoreInteractions(keycloakService);
    }

    @Test(expected = EntityNotFoundException.class)
    public void shouldThrowExceptionWhenTeamDoesntExsist(){
        UUID teamUUID = UUID.randomUUID();
        when(teamRepository.findByUuid(teamUUID)).thenReturn(null);

        service.getUsersForTeam(teamUUID.toString());
    }

}