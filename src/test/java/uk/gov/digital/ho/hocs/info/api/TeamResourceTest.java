package uk.gov.digital.ho.hocs.info.api;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uk.gov.digital.ho.hocs.info.api.TeamResource;
import uk.gov.digital.ho.hocs.info.api.TeamService;
import uk.gov.digital.ho.hocs.info.api.dto.PermissionDto;
import uk.gov.digital.ho.hocs.info.api.dto.TeamDto;
import uk.gov.digital.ho.hocs.info.api.dto.UpdateTeamPermissionsRequest;
import uk.gov.digital.ho.hocs.info.domain.exception.ApplicationExceptions;
import uk.gov.digital.ho.hocs.info.domain.model.Team;
import uk.gov.digital.ho.hocs.info.security.AccessLevel;

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

    @Test(expected = ApplicationExceptions.TeamDeleteException.class)
    public void shouldThrowTeamDeleteExceptionWhenDeleteATeamWithActiveParentTopics() {

        doThrow(ApplicationExceptions.TeamDeleteException.class)
                .when(teamService)
                .deleteTeam(teamUUID);

        teamResource.deleteTeam(teamUUID.toString());
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
        UUID unitUUID = UUID.randomUUID();
        Team team = new Team("Team1", true);
        when(teamService.getTeam(team.getUuid())).thenReturn(team);

        ResponseEntity<TeamDto> result = teamResource.getTeam(unitUUID.toString(), team.getUuid().toString());
        assertThat(result.getBody().getUuid()).isEqualTo(team.getUuid());
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(teamService, times(1)).getTeam(team.getUuid());
        verifyNoMoreInteractions(teamService);
    }

    @Test(expected = ApplicationExceptions.EntityNotFoundException.class)
    public void returnNotFoundWhenTeamDoesNotExist() {
        UUID teamUUID = UUID.randomUUID();
        UUID unitUUID = UUID.randomUUID();
        when(teamService.getTeam(teamUUID)).thenThrow(new ApplicationExceptions.EntityNotFoundException(""));

        teamResource.getTeam(unitUUID.toString(), teamUUID.toString());
        verify(teamService, times(1)).getTeam(teamUUID);
        verifyNoMoreInteractions(teamService);
    }

    @Test
    public void shouldCreateNewTeam() {
        UUID teamUUID = UUID.randomUUID();
        UUID unitUUID = UUID.randomUUID();
        Team team = new Team("Team1", true);
        TeamDto teamDto = TeamDto.from(team);
        when(teamService.createTeam(teamDto, unitUUID)).thenReturn(team);

        ResponseEntity result = teamResource.createUpdateTeam(unitUUID.toString(), teamDto);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);

        verify(teamService, times(1)).createTeam(teamDto, unitUUID);
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

    @Test
    public void shouldUpdateRequestedPermissionsFromATeam() {
        UUID teamUUID = UUID.randomUUID();
        Set<PermissionDto> permissionDtoSet = new HashSet<>();
        permissionDtoSet.add(new PermissionDto("CT3", AccessLevel.WRITE));
        UpdateTeamPermissionsRequest request = new UpdateTeamPermissionsRequest(permissionDtoSet);

        doNothing().when(teamService).updateTeamPermissions(teamUUID, permissionDtoSet);

        ResponseEntity result = teamResource.updateTeamPermissions(teamUUID.toString(), request);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(teamService, times(1)).updateTeamPermissions(teamUUID, permissionDtoSet);
        verifyNoMoreInteractions(teamService);
    }

    @Test
    public void shouldRemoveRequestedPermissionsFromATeam() {
        UUID teamUUID = UUID.randomUUID();
        Set<PermissionDto> permissionDtoSet = new HashSet<>();
        permissionDtoSet.add(new PermissionDto("CT3", AccessLevel.WRITE));
        UpdateTeamPermissionsRequest request = new UpdateTeamPermissionsRequest(permissionDtoSet);

        doNothing().when(teamService).deleteTeamPermissions(teamUUID, permissionDtoSet);

        ResponseEntity result = teamResource.deleteTeamPermissions(teamUUID.toString(), request);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(teamService, times(1)).deleteTeamPermissions(teamUUID, permissionDtoSet);
        verifyNoMoreInteractions(teamService);
    }

    private Set<Team> getTeams() {
        return new HashSet<Team>() {{
            add(new Team("Team1", true));
            add(new Team("Team2", true));
        }};
    }

}