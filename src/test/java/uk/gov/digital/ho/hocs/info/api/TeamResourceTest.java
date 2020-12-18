package uk.gov.digital.ho.hocs.info.api;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uk.gov.digital.ho.hocs.info.api.dto.*;
import uk.gov.digital.ho.hocs.info.domain.exception.ApplicationExceptions;
import uk.gov.digital.ho.hocs.info.domain.model.Team;
import uk.gov.digital.ho.hocs.info.domain.model.Unit;
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
    private UUID caseUUID = UUID.randomUUID();

    @Test
    public void shouldAddUserToTeam() {
        doNothing().when(teamService).addUserToTeam(userUUID, teamUUID);

        ResponseEntity result = teamResource.addUserToGroup(userUUID.toString(), teamUUID.toString());
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(teamService).addUserToTeam(userUUID, teamUUID);
        verifyNoMoreInteractions(teamService);
    }

    @Test
    public void shouldDeleteATeam() {
        doNothing().when(teamService).deleteTeam(teamUUID);
        ResponseEntity result = teamResource.deleteTeam(teamUUID.toString());
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(teamService).deleteTeam(teamUUID);
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
        verify(teamService).getTeamsForUnit(unitUUID);
        verifyNoMoreInteractions(teamService);
    }

    @Test
    public void shouldGetAllTeamsForAUser() {
        UUID userUUID = UUID.randomUUID();
        when(teamService.getTeamsForUser(userUUID)).thenReturn(getTeams());

        ResponseEntity<Set<TeamDto>> result = teamResource.getTeamsForUser(userUUID.toString());
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody().size()).isEqualTo(2);
        verify(teamService).getTeamsForUser(userUUID);
        verifyNoMoreInteractions(teamService);
    }

    @Test
    public void shouldGetAllTeams() {

        when(teamService.getAllTeams()).thenReturn(getAllTeams());

        ResponseEntity<Set<TeamDto>> result = teamResource.getAllTeams();
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody().size()).isEqualTo(3);
        verify(teamService).getAllTeams();
        verifyNoMoreInteractions(teamService);
    }

    @Test
    public void shouldGetAllActiveTeams() {

        when(teamService.getAllActiveTeams()).thenReturn(getTeams());

        ResponseEntity<Set<TeamDto>> result = teamResource.getActiveTeams();
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody().size()).isEqualTo(2);
        verify(teamService).getAllActiveTeams();
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
        verify(teamService).getTeam(team.getUuid());
        verifyNoMoreInteractions(teamService);
    }

    @Test(expected = ApplicationExceptions.EntityNotFoundException.class)
    public void returnNotFoundWhenTeamDoesNotExist() {
        UUID teamUUID = UUID.randomUUID();
        UUID unitUUID = UUID.randomUUID();
        when(teamService.getTeam(teamUUID)).thenThrow(new ApplicationExceptions.EntityNotFoundException(""));

        teamResource.getTeam(unitUUID.toString(), teamUUID.toString());
        verify(teamService).getTeam(teamUUID);
        verifyNoMoreInteractions(teamService);
    }

    @Test
    public void shouldGetTeamForTeamUUID() {
        Team team = new Team("Team1", true);
        when(teamService.getTeam(team.getUuid())).thenReturn(team);

        ResponseEntity<TeamDto> result = teamResource.getTeam(team.getUuid());
        assertThat(result.getBody().getUuid()).isEqualTo(team.getUuid());
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(teamService).getTeam(team.getUuid());
        verifyNoMoreInteractions(teamService);
    }

    @Test(expected = ApplicationExceptions.EntityNotFoundException.class)
    public void returnNotFoundWhenTeamDoesNotExistForTeamUUID() {
        UUID teamUUID = UUID.randomUUID();
        when(teamService.getTeam(teamUUID)).thenThrow(new ApplicationExceptions.EntityNotFoundException(""));

        teamResource.getTeam(teamUUID);
        verify(teamService).getTeam(teamUUID);
        verifyNoMoreInteractions(teamService);
    }

    @Test
    public void getUnitByTeam() {

        UUID unitUUID = UUID.randomUUID();
        UUID teamUUID = UUID.randomUUID();
        Unit unit = mock(Unit.class);
        Team team = mock(Team.class);
        when(teamService.getTeam(teamUUID)).thenReturn(team);
        when(team.getUnit()).thenReturn(unit);
        when(unit.getUuid()).thenReturn(unitUUID);

        ResponseEntity<UnitDto> result = teamResource.getUnitByTeam(teamUUID);
        assertThat(result.getBody().getUuid()).isEqualTo(unitUUID.toString());
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(teamService).getTeam(teamUUID);
        verifyNoMoreInteractions(teamService);
    }

    @Test(expected = ApplicationExceptions.EntityNotFoundException.class)
    public void getUnitByTeam_returnNotFoundWhenTeamDoesNotExist() {
        UUID teamUUID = UUID.randomUUID();
        when(teamService.getTeam(teamUUID)).thenThrow(new ApplicationExceptions.EntityNotFoundException(""));

        teamResource.getUnitByTeam(teamUUID);
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

        verify(teamService).createTeam(teamDto, unitUUID);
        verifyNoMoreInteractions(teamService);
    }

    @Test
    public void shouldUpdateTeamName() {
        UUID teamUUID = UUID.randomUUID();
        UpdateTeamNameRequest request = new UpdateTeamNameRequest("The Team");
        ResponseEntity result = teamResource.updateTeamName(teamUUID.toString(), request);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);

        verify(teamService).updateTeamName(teamUUID, request.getDisplayName());
        verifyNoMoreInteractions(teamService);
    }

    @Test
    public void shouldUpdateTeamLetterName() {
        UUID teamUUID = UUID.randomUUID();
        UpdateTeamLetterNameRequest request = new UpdateTeamLetterNameRequest("Bob");
        ResponseEntity result = teamResource.updateTeamLetterName(teamUUID.toString(), request);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);

        verify(teamService).updateTeamLetterName(teamUUID, request.getLetterName());
        verifyNoMoreInteractions(teamService);
    }

    @Test
    public void shouldMoveTeamBetweenUnits() {
        UUID teamUUID = UUID.randomUUID();
        UUID unitUUID = UUID.randomUUID();

        ResponseEntity result = teamResource.addTeamToUnit(unitUUID.toString(), teamUUID.toString());
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(teamService).moveToNewUnit(unitUUID, teamUUID);
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
        verify(teamService).updateTeamPermissions(teamUUID, permissionDtoSet);
        verifyNoMoreInteractions(teamService);
    }

    @Test
    public void shouldGetTeamsByTopic() {
        UUID topicUuid = UUID.randomUUID();
        Set<Team> teams = Set.of(new Team(UUID.randomUUID().toString(), true));
        when(teamService.getTeamsByTopic(topicUuid)).thenReturn(teams);

        ResponseEntity<Set<TeamDto>> response = teamResource.getTeamsByTopic(topicUuid);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().iterator().next().getUuid()).isEqualTo(teams.iterator().next().getUuid());
        verify(teamService).getTeamsByTopic(topicUuid);
        verifyNoMoreInteractions(teamService);
    }

    @Test
    public void shouldGetActiveTeamsByLinkValue() {
        Team team = new Team("Team1", true);
        when(teamService.getTeamByStageAndText("stageType", "text")).thenReturn(team);

        ResponseEntity<TeamDto> response = teamResource.getActiveTeamsByLinkValue("stageType", "text");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getUuid()).isEqualTo(team.getUuid());
        verify(teamService).getTeamByStageAndText("stageType", "text");
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
        verify(teamService).deleteTeamPermissions(teamUUID, permissionDtoSet);
        verifyNoMoreInteractions(teamService);
    }

    private Set<Team> getTeams() {
        return new HashSet<Team>() {{
            add(new Team("Team1", true));
            add(new Team("Team2", true));
        }};
    }

    private Set<Team> getAllTeams() {
        return new HashSet<Team>() {{
            add(new Team("Team1", true));
            add(new Team("Team2", true));
            add(new Team("Team3", false));
        }};
    }

    @Test
    public void shouldRemoveUserFromTeam() {
        UUID teamUUID = UUID.randomUUID();
        UUID userUUID = UUID.randomUUID();

        ResponseEntity result = teamResource.removeUserFromTeam(userUUID.toString(), teamUUID.toString());
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(teamService).removeUserFromTeam(userUUID, teamUUID);
        verifyNoMoreInteractions(teamService);
    }

}
