package uk.gov.digital.ho.hocs.info.team;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uk.gov.digital.ho.hocs.info.dto.TeamDto;
import uk.gov.digital.ho.hocs.info.exception.EntityNotFoundException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TeamResourceTest {

    @Mock
    private TeamService teamService;

    private TeamResource teamResource;

    @Before
    public void setUp() {
        teamResource = new TeamResource(teamService);
    }

    private UUID userUUID = UUID.randomUUID();
    private UUID teamUUID = UUID.randomUUID();

    @Test
    public void shouldAddUserToTeam() {
        doNothing().when(teamService).addUserToTeam(userUUID, teamUUID);

        ResponseEntity result = teamResource.addUserToGroup(userUUID.toString(), teamUUID.toString());
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(teamService, times(1)).addUserToTeam(userUUID, teamUUID);
        verifyNoMoreInteractions(teamService);
    }

    @Test
    public void shouldDeleteATeam() {
        doNothing().when(teamService).deleteTeam(teamUUID);
        ResponseEntity result = teamResource.deleteTeam(teamUUID.toString());
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(teamService, times(1)).deleteTeam(teamUUID);
        verifyNoMoreInteractions(teamService);
    }

    @Test
    public void shouldGetAllTeamsForAUnit() {
        UUID unitUUID = UUID.randomUUID();
        when(teamService.getTeamsForUnit(unitUUID)).thenReturn(getTeams());

        ResponseEntity<Set<TeamDto>> result = teamResource.getTeamsForUnit(unitUUID.toString());
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody().size()).isEqualTo(2);
        verify(teamService, times(1)).getTeamsForUnit(unitUUID);
        verifyNoMoreInteractions(teamService);
    }
    @Test
    public void shouldGetAllActiveTeams() {

        when(teamService.getAllActiveTeams()).thenReturn(getTeams());

        ResponseEntity<Set<TeamDto>> result = teamResource.getActiveTeams();
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody().size()).isEqualTo(2);
        verify(teamService, times(1)).getAllActiveTeams();
        verifyNoMoreInteractions(teamService);
    }


    @Test
    public void shouldGetTeamForUUID() {
        UUID teamUUID = UUID.randomUUID();
        UUID unitUUID = UUID.randomUUID();
        TeamDto team = new TeamDto( "Team1", teamUUID, true, new HashSet<>());
        when(teamService.getTeam(teamUUID)).thenReturn(team);

        ResponseEntity<TeamDto> result = teamResource.getTeam(unitUUID.toString(), teamUUID.toString());
        assertThat(result.getBody().getUuid()).isEqualTo(teamUUID);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(teamService, times(1)).getTeam(teamUUID);
        verifyNoMoreInteractions(teamService);
    }

    @Test(expected = EntityNotFoundException.class)
    public void returnNotFoundWHenTeamDoesNotExist() {
        UUID teamUUID = UUID.randomUUID();
        UUID unitUUID = UUID.randomUUID();
        when(teamService.getTeam(teamUUID)).thenThrow(new EntityNotFoundException(""));

        teamResource.getTeam(unitUUID.toString(), teamUUID.toString());
        verify(teamService, times(1)).getTeam(teamUUID);
        verifyNoMoreInteractions(teamService);
    }

    @Test
    public void shouldCreateNewTeam() {
        UUID teamUUID = UUID.randomUUID();
        UUID unitUUID = UUID.randomUUID();
        TeamDto team = new TeamDto( "Team1", teamUUID, true, new HashSet<>());
        when(teamService.createTeam(team, unitUUID)).thenReturn(team);

        ResponseEntity result = teamResource.createUpdateTeam(unitUUID.toString(), team);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);

        verify(teamService, times(1)).createTeam(team, unitUUID);
        verifyNoMoreInteractions(teamService);
    }

    @Test
    public void shouldMoveTeamBetweenUnits() {
        UUID teamUUID = UUID.randomUUID();
        UUID unitUUID = UUID.randomUUID();

        ResponseEntity result = teamResource.addTeamToUnit(unitUUID.toString(), teamUUID.toString());
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(teamService, times(1)).moveToNewUnit(unitUUID, teamUUID);
        verifyNoMoreInteractions(teamService);
    }

    private Set<TeamDto> getTeams() {
        return new HashSet<TeamDto>() {{
            add(new TeamDto( "Team1", UUID.randomUUID(), true, new HashSet<>()));
            add(new TeamDto( "Team2", UUID.randomUUID(), true, new HashSet<>()));
        }};
    }

}