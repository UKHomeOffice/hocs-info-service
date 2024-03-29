package uk.gov.digital.ho.hocs.info.api;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.digital.ho.hocs.info.api.dto.PatchTeamDto;
import uk.gov.digital.ho.hocs.info.api.dto.PermissionDto;
import uk.gov.digital.ho.hocs.info.api.dto.TeamDto;
import uk.gov.digital.ho.hocs.info.client.audit.client.AuditClient;
import uk.gov.digital.ho.hocs.info.client.caseworkclient.CaseworkClient;
import uk.gov.digital.ho.hocs.info.client.notifyclient.NotifyClient;
import uk.gov.digital.ho.hocs.info.domain.exception.ApplicationExceptions;
import uk.gov.digital.ho.hocs.info.domain.model.*;
import uk.gov.digital.ho.hocs.info.domain.repository.ParentTopicRepository;
import uk.gov.digital.ho.hocs.info.domain.repository.TeamRepository;
import uk.gov.digital.ho.hocs.info.domain.repository.UnitRepository;
import uk.gov.digital.ho.hocs.info.security.AccessLevel;
import uk.gov.digital.ho.hocs.info.security.KeycloakService;

import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)

public class TeamServiceTest {

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private UnitRepository unitRepository;

    @Mock
    private CaseTypeService caseTypeService;

    @Mock
    private ParentTopicRepository parentTopicRepository;

    @Mock
    private KeycloakService keycloakService;

    @Mock
    private AuditClient auditClient;

    @Mock
    private CaseworkClient caseworkClient;

    @Mock
    private NotifyClient notifyClient;

    private TeamService teamService;

    @Before
    public void setUp() {
        this.teamService = new TeamService(teamRepository, unitRepository, caseTypeService, parentTopicRepository,
            keycloakService, auditClient, caseworkClient, notifyClient);
    }

    private UUID team1UUID = UUID.randomUUID();

    @Test
    public void shouldGetAllTeams() {

        when(teamRepository.findAll()).thenReturn(getAllTeams().stream().collect(Collectors.toSet()));
        Set<Team> result = teamService.getAllTeams();
        assertThat(result).size().isEqualTo(3);
        verify(teamRepository, times(1)).findAll();
        verifyNoMoreInteractions(teamRepository);
    }

    @Test
    public void shouldGetAllActiveTeams() {

        when(teamRepository.findAllByActiveTrue()).thenReturn(getTeams().stream().collect(Collectors.toSet()));
        Set<Team> result = teamService.getAllActiveTeams();
        assertThat(result).size().isEqualTo(2);
        verify(teamRepository, times(1)).findAllByActiveTrue();
        verifyNoMoreInteractions(teamRepository);
    }

    @Test
    public void shouldGetAllTeamsForUnit() {
        UUID unitUUID = UUID.randomUUID();
        when(teamRepository.findTeamsByUnitUuid(unitUUID)).thenReturn(getTeams().stream().collect(Collectors.toSet()));
        Set<Team> result = teamService.getTeamsForUnit(unitUUID);
        assertThat(result).size().isEqualTo(2);
        verify(teamRepository, times(1)).findTeamsByUnitUuid(unitUUID);
        verifyNoMoreInteractions(teamRepository);
    }

    @Test
    public void shouldGetTeamById() {
        Team team = new Team("Team1", new HashSet<>());
        when(teamRepository.findByUuid(team.getUuid())).thenReturn(team);
        teamService.getTeam(team.getUuid());
        verify(teamRepository, times(1)).findByUuid(team.getUuid());
        verifyNoMoreInteractions(teamRepository);
    }

    @Test(expected = ApplicationExceptions.EntityNotFoundException.class)
    public void throwExceptionWhenTeamDoesNotExist() {
        when(teamRepository.findByUuid(team1UUID)).thenThrow(new ApplicationExceptions.EntityNotFoundException(""));
        teamService.getTeam(team1UUID);
        verify(teamRepository, times(1)).findByUuid(team1UUID);
        verifyNoMoreInteractions(teamRepository);
    }

    @Test
    public void shouldUpdateName() {
        String newTeamName = "Test";
        Team team = mock(Team.class);
        when(teamRepository.findByDisplayName(any())).thenReturn(null);
        when(teamRepository.findByUuid(team1UUID)).thenReturn(team);
        when(team.getUuid()).thenReturn(team1UUID);
        when(team.getDisplayName()).thenReturn("__oldName__");
        teamService.updateTeamName(team1UUID, newTeamName);

        verify(teamRepository, times(1)).findByDisplayName(newTeamName);
        verify(teamRepository, times(1)).findByUuid(team1UUID);
        verify(team, times(1)).setDisplayName(newTeamName);

        verify(auditClient).renameTeamAudit(any());
        verify(notifyClient).sendTeamRenameEmail(team1UUID, "__oldName__");
        verifyNoMoreInteractions(teamRepository);
    }

    @Test(expected = ApplicationExceptions.EntityAlreadyExistsException.class)
    public void shouldUpdateName_throwWhenTeamWithNameExists() {
        Team team = mock(Team.class);

        when(teamRepository.findByDisplayName("Test")).thenReturn(team);

        teamService.updateTeamName(team1UUID, "Test");
    }

    @Test
    public void shouldUpdateLetterName() {
        Team team = mock(Team.class);
        when(teamRepository.findByUuid(team1UUID)).thenReturn(team);
        when(team.getUuid()).thenReturn(team1UUID);
        teamService.updateTeamLetterName(team1UUID, "new name");

        verify(teamRepository, times(1)).findByUuid(team1UUID);
        verify(team, times(1)).setLetterName("new name");
        verifyNoMoreInteractions(teamRepository);
    }

    @Test
    public void shouldAddTeamToRepositoryAndKeycloak() {

        Unit unit = new Unit("UNIT1", "UNIT1", true);

        TeamDto teamDto = new TeamDto("Team1", new HashSet<>());

        when(teamRepository.findByDisplayName(any())).thenReturn(null);
        when(unitRepository.findByUuid(unit.getUuid())).thenReturn(unit);

        Team result = teamService.createTeam(teamDto, unit.getUuid());

        verify(teamRepository).findByDisplayName(any());
        verify(unitRepository).findByUuid(unit.getUuid());
        verify(keycloakService).createTeamGroupIfNotExists(result.getUuid());
        verifyNoMoreInteractions(teamRepository);
        verifyNoMoreInteractions(keycloakService);
    }

    @Test
    public void shouldAddTeamToKeycloakIfTeamExists() {
        Team team = new Team("TEAM 4", true);
        Unit unit = new Unit("UNIT1", "UNIT1", true);
        unit.addTeam(team);

        TeamDto teamDto = new TeamDto("TEAM 1", null, UUID.randomUUID(), true, new HashSet<>(), null);
        when(unitRepository.findByUuid(unit.getUuid())).thenReturn(unit);

        try {
            teamService.createTeam(teamDto, unit.getUuid());
        } catch (ApplicationExceptions.EntityAlreadyExistsException e) {
            assertThat(e.getMessage()).isEqualTo("Team: TEAM 4 already exists.");
        }
        verify(unitRepository).findByUuid(unit.getUuid());
        verify(unitRepository, never()).save(unit);
        verify(keycloakService).createTeamGroupIfNotExists(any());
        verifyNoMoreInteractions(keycloakService);
    }

    @Test
    public void shouldMarkTeamAsInactive() {
        Team team = new Team("Team1", true);
        when(teamRepository.findByUuid(team1UUID)).thenReturn(team);
        assertThat(team.isActive()).isTrue();
        teamService.deleteTeam(team1UUID);
        assertThat(team.isActive()).isFalse();
    }

    @Test
    public void shouldGetTeamsForUser() {
        Set<Team> teams = new HashSet<>();
        Team team = new Team(UUID.randomUUID().toString(), true);
        teams.add(team);
        UUID userId = UUID.randomUUID();
        UUID teamId = UUID.randomUUID();
        Set<UUID> teamUUIDs = new HashSet<>();
        teamUUIDs.add(teamId);

        when(keycloakService.getGroupsForUser(userId)).thenReturn(teamUUIDs);
        when(teamRepository.findByUuid(teamId)).thenReturn(team);
        Set<Team> result = teamService.getTeamsForUser(userId);

        assertThat(result).isEqualTo(teams);
    }

    @Test
    public void shouldGetTeamsByTopic() {
        UUID topicUuid = UUID.randomUUID();
        Set<Team> teams = Set.of(new Team(UUID.randomUUID().toString(), true));
        when(teamRepository.findTeamsByTopicUuid(topicUuid)).thenReturn(teams);

        Set<Team> result = teamService.getTeamsByTopic(topicUuid);

        assertThat(result).isEqualTo(teams);
    }

    @Test
    public void shouldGetTeamByStageAndText() {
        Team team = new Team(UUID.randomUUID().toString(), true);
        when(teamRepository.findByStageAndText("stageType", "text")).thenReturn(team);

        Team result = teamService.getTeamByStageAndText("stageType", "text");

        assertThat(result).isEqualTo(team);
    }

    @Test
    public void shouldGetActiveTeamsByStageType() {
        Set<Team> teams = Set.of(new Team(UUID.randomUUID().toString(), true));
        when(teamRepository.findActiveByStageType("stageType")).thenReturn(teams);

        Set<Team> result = teamService.getActiveTeamsByStageType("stageType");

        assertThat(result).isEqualTo(teams);
    }

    @Test
    public void shouldGetTeamPermissions() {
        when(teamRepository.findByUuid(team1UUID)).thenReturn(getTeams().get(0));
        Team result = teamService.getTeam(team1UUID);
        assertThat(result.getPermissions().size()).isEqualTo(1);
        verify(teamRepository, times(1)).findByUuid(team1UUID);
        verifyNoMoreInteractions(teamRepository);
    }

    @Test
    public void shouldMoveUserBetweenTeam() {
        UUID unitUUID = UUID.randomUUID();
        UUID oldUnitUUID = UUID.randomUUID();

        Team team = mock(Team.class);
        Unit unit = mock(Unit.class);
        Unit oldUnit = mock(Unit.class);

        when(teamRepository.findByUuid(team1UUID)).thenReturn(team);
        when(unitRepository.findByUuid(oldUnitUUID)).thenReturn(oldUnit);
        when(unitRepository.findByUuid(unitUUID)).thenReturn(unit);
        when(oldUnit.getUuid()).thenReturn(oldUnitUUID);
        when(team.getUnit()).thenReturn(oldUnit);
        when(team.getUnit().getShortCode()).thenReturn("UNIT1");

        teamService.moveToNewUnit(unitUUID, team1UUID);

        verify(unit, times(1)).addTeam(team);
        verify(oldUnit, times(1)).removeTeam(team1UUID);

    }

    @Test
    public void shouldAddUsersToTeam() {
        UUID userUUID1 = UUID.randomUUID();
        UUID userUUID2 = UUID.randomUUID();

        Set<Permission> permissions = new HashSet<>();
        Unit unit = new Unit("a unit", "UNIT", true);
        CaseType caseType = new CaseType(1L, UUID.randomUUID(), "MIN", "a1", "MIN", UUID.randomUUID(),
            "DCU_MIN_DISPATCH", true, true, null, null);
        Permission permission = new Permission(AccessLevel.OWNER, null, caseType);
        permissions.add(permission);
        Team team = new Team("a team", true);
        team.setUnit(unit);
        team.addPermissions(permissions);

        when(teamRepository.findByUuid(team.getUuid())).thenReturn(team);
        teamService.addUsersToTeam(List.of(userUUID1, userUUID2), team.getUuid());
        verify(keycloakService, times(1)).createTeamGroupIfNotExists(team.getUuid());
        verify(keycloakService, times(1)).addUserToTeam(userUUID1, team.getUuid());
        verify(keycloakService, times(1)).addUserToTeam(userUUID2, team.getUuid());
    }

    @Test
    public void shouldRemoveUserWithNoCasesFromTeam() {
        Team team = new Team("a team", true);
        UUID userUUID = UUID.randomUUID();
        UUID teamUUID = UUID.randomUUID();

        when(caseworkClient.getCasesForUser(userUUID, teamUUID)).thenReturn(new HashSet<>());

        teamService.removeUserFromTeam(userUUID, teamUUID);
        verify(keycloakService, times(1)).removeUserFromTeam(userUUID, teamUUID);
        verifyNoMoreInteractions(keycloakService);
    }

    @Test(expected = ApplicationExceptions.UserRemoveException.class)
    public void shouldNotRemoveUserWithCasesFromTeam() {
        Team team = new Team("a team", true);
        UUID userUUID = UUID.randomUUID();
        UUID teamUUID = UUID.randomUUID();

        Set<UUID> casesSet = new HashSet<>();
        casesSet.add(UUID.randomUUID());
        when(caseworkClient.getCasesForUser(userUUID, teamUUID)).thenReturn(casesSet);

        teamService.removeUserFromTeam(userUUID, teamUUID);

        verifyNoMoreInteractions(keycloakService);
    }

    @Test
    public void shouldUpdatePermissionsInDatabase() {

        Unit unit = new Unit("a unit", "UNIT", true);
        Team team = new Team("a team", true);
        team.setUnit(unit);

        CaseType caseType = new CaseType(1L, UUID.randomUUID(), "MIN", "a1", "MIN", UUID.randomUUID(),
            "DCU_MIN_DISPATCH", true, true, null, null);

        when(teamRepository.findByUuid(team.getUuid())).thenReturn(team);
        when(caseTypeService.getCaseType(any())).thenReturn(caseType);

        Set<PermissionDto> permissions = new HashSet<PermissionDto>() {{
            add(new PermissionDto("MIN", AccessLevel.READ));
            add(new PermissionDto("MIN", AccessLevel.OWNER));
        }};

        assertThat(team.getPermissions().size()).isEqualTo(0);
        teamService.updateTeamPermissions(team.getUuid(), permissions);
        assertThat(team.getPermissions().size()).isEqualTo(2);

        verify(teamRepository, times(1)).findByUuid(team.getUuid());
        verifyNoMoreInteractions(teamRepository);
    }

    @Test
    public void shouldDeleteSinglePermissionInDatabase() {

        Set<Permission> permissions = new HashSet<>();
        Unit unit = new Unit("a unit", "UNIT", true);
        Team team = new Team("a team", true);
        team.setUnit(unit);
        CaseType caseType = new CaseType(1L, UUID.randomUUID(), "MIN", "a1", "MIN", UUID.randomUUID(),
            "DCU_MIN_DISPATCH", true, true, null, null);
        permissions.add(new Permission(AccessLevel.READ, team, caseType));
        permissions.add(new Permission(AccessLevel.OWNER, team, caseType));
        team.addPermissions(permissions);

        when(teamRepository.findByUuid(team.getUuid())).thenReturn(team);
        when(caseTypeService.getCaseType(any())).thenReturn(caseType);

        Set<PermissionDto> permissionDtoSet = new HashSet<PermissionDto>() {{
            add(new PermissionDto("MIN", AccessLevel.READ));
        }};

        assertThat(team.getPermissions().size()).isEqualTo(2);
        teamService.deleteTeamPermissions(team.getUuid(), permissionDtoSet);
        assertThat(team.getPermissions().size()).isEqualTo(1);

        verify(teamRepository, times(1)).findByUuid(team.getUuid());
        verifyNoMoreInteractions(teamRepository);
    }

    @Test
    public void shouldDeleteMultiplePermissionInDatabaseAndKeycloak() {

        UUID unitUUID = UUID.randomUUID();
        Set<Permission> permissions = new HashSet<>();
        Unit unit = new Unit("a unit", "UNIT", true);
        Team team = new Team("a team", true);
        team.setUnit(unit);
        CaseType caseType = new CaseType(1L, UUID.randomUUID(), "MIN", "a1", "MIN", UUID.randomUUID(),
            "DCU_MIN_DISPATCH", true, true, null, null);
        permissions.add(new Permission(AccessLevel.READ, team, caseType));
        permissions.add(new Permission(AccessLevel.OWNER, team, caseType));
        team.addPermissions(permissions);

        when(teamRepository.findByUuid(team1UUID)).thenReturn(team);
        when(caseTypeService.getCaseType(any())).thenReturn(caseType);

        Set<PermissionDto> permissionDtoSet = new HashSet<PermissionDto>() {{
            add(new PermissionDto("MIN", AccessLevel.READ));
            add(new PermissionDto("MIN", AccessLevel.OWNER));
        }};

        assertThat(team.getPermissions().size()).isEqualTo(2);
        teamService.deleteTeamPermissions(team1UUID, permissionDtoSet);
        assertThat(team.getPermissions().size()).isEqualTo(0);

        verify(teamRepository, times(1)).findByUuid(team1UUID);
        verifyNoMoreInteractions(teamRepository);
    }

    @Test
    public void ShouldDeleteTeamWhenNoActiveParentTopicsAreLinkedToIt() {
        Team team = new Team("a team", true);
        when(parentTopicRepository.findAllActiveParentTopicsForTeam(team.getUuid())).thenReturn(new ArrayList<>());
        when(teamRepository.findByUuid(team.getUuid())).thenReturn(team);

        assertThat(team.isActive()).isTrue();
        teamService.deleteTeam(team.getUuid());
        assertThat(team.isActive()).isFalse();

        verify(parentTopicRepository, times(1)).findAllActiveParentTopicsForTeam(team.getUuid());
        verify(teamRepository, times(1)).findByUuid(team.getUuid());
        verifyNoMoreInteractions(parentTopicRepository);
        verifyNoMoreInteractions(teamRepository);
    }

    @Test(expected = ApplicationExceptions.TeamDeleteException.class)
    public void shouldThrowTeamDeleteExceptionWhenActiveParentTopicsAreLinkedToTeam() {
        List<ParentTopic> parentTopicsList = new ArrayList<>();
        parentTopicsList.add(new ParentTopic());
        when(parentTopicRepository.findAllActiveParentTopicsForTeam(team1UUID)).thenReturn(parentTopicsList);
        teamService.deleteTeam(team1UUID);
    }

    @Test
    public void ShouldAuditCreateTeam() {

        Unit unit = new Unit("UNIT1", "UNIT1", true);

        TeamDto teamDto = new TeamDto("Team1", new HashSet<>());

        when(unitRepository.findByUuid(unit.getUuid())).thenReturn(unit);
        teamService.createTeam(teamDto, unit.getUuid());

        verify(auditClient, times(1)).createTeamAudit(any(Team.class));
    }

    @Test
    public void ShouldAuditUpdateTeamName() {
        Team team = new Team("Team1", true);

        when(teamRepository.findByUuid(team.getUuid())).thenReturn(team);
        teamService.updateTeamName(team.getUuid(), "");

        verify(auditClient, times(1)).renameTeamAudit(team);
    }

    @Test
    public void ShouldAuditAddUserToTeam() {
        UUID userUUID1 = UUID.randomUUID();
        UUID userUUID2 = UUID.randomUUID();

        Set<Permission> permissions = new HashSet<>();
        Unit unit = new Unit("a unit", "UNIT", true);
        CaseType caseType = new CaseType(1L, UUID.randomUUID(), "MIN", "a1", "MIN", UUID.randomUUID(),
            "DCU_MIN_DISPATCH", true, true, null, null);
        Permission permission = new Permission(AccessLevel.OWNER, null, caseType);
        permissions.add(permission);
        Team team = new Team("a team", true);
        team.setUnit(unit);

        when(teamRepository.findByUuid(team.getUuid())).thenReturn(team);
        teamService.addUsersToTeam(List.of(userUUID1, userUUID2), team.getUuid());
        verify(auditClient, times(1)).addUsersToTeamAudit(userUUID1 + "," + userUUID2, team);
    }

    @Test
    public void ShouldAuditMoveToNewUnit() {
        UUID newUnitUUID = UUID.randomUUID();
        UUID oldUnitUUID = UUID.randomUUID();

        Team team = mock(Team.class);
        Unit newUnit = mock(Unit.class);
        Unit oldUnit = mock(Unit.class);

        when(teamRepository.findByUuid(team1UUID)).thenReturn(team);
        when(unitRepository.findByUuid(oldUnitUUID)).thenReturn(oldUnit);
        when(unitRepository.findByUuid(newUnitUUID)).thenReturn(newUnit);
        when(oldUnit.getUuid()).thenReturn(oldUnitUUID);
        when(oldUnit.getShortCode()).thenReturn("UNIT1");
        when(newUnit.getShortCode()).thenReturn("UNIT2");

        when(team.getUnit()).thenReturn(oldUnit);

        teamService.moveToNewUnit(newUnitUUID, team1UUID);

        verify(auditClient, times(1)).moveToNewUnitAudit(team1UUID.toString(), "UNIT1", "UNIT2");

    }

    @Test
    public void ShouldSetActiveFlag() {
        Team team = mock(Team.class);

        when(teamRepository.findByUuid(team1UUID)).thenReturn(team);
        when(team.getUuid()).thenReturn(team1UUID);

        teamService.setTeamActiveFlag(team1UUID, false);

        verify(auditClient, times(1)).setTeamActivationFlag(team);
        verify(notifyClient).sendTeamActiveStatusEmail(team1UUID, Boolean.FALSE);

    }

    @Test
    public void ShouldAuditUpdateTeamPermissions() {
        Unit unit = new Unit("a unit", "UNIT", true);
        Team team = new Team("a team", true);
        team.setUnit(unit);

        CaseType caseType = new CaseType(1L, UUID.randomUUID(), "MIN", "a1", "MIN", UUID.randomUUID(),
            "DCU_MIN_DISPATCH", true, true, null, null);

        when(teamRepository.findByUuid(team.getUuid())).thenReturn(team);
        when(caseTypeService.getCaseType(any())).thenReturn(caseType);

        Set<PermissionDto> permissions = new HashSet<>() {{
            add(new PermissionDto("MIN", AccessLevel.READ));
            add(new PermissionDto("MIN", AccessLevel.OWNER));
        }};

        teamService.updateTeamPermissions(team.getUuid(), permissions);

        verify(auditClient, times(1)).updateTeamPermissionsAudit(permissions);
    }

    @Test
    public void ShouldAuditDeleteTeamPermissions() {
        Set<Permission> permissions = new HashSet<>();
        Unit unit = new Unit("a unit", "UNIT", true);
        Team team = new Team("a team", true);
        team.addPermissions(permissions);
        team.setUnit(unit);
        CaseType caseType = new CaseType(1L, UUID.randomUUID(), "MIN", "a1", "MIN", UUID.randomUUID(),
            "DCU_MIN_DISPATCH", true, true, null, null);
        permissions.add(new Permission(AccessLevel.READ, team, caseType));
        permissions.add(new Permission(AccessLevel.OWNER, team, caseType));

        when(teamRepository.findByUuid(team.getUuid())).thenReturn(team);
        when(caseTypeService.getCaseType(any())).thenReturn(caseType);

        Set<PermissionDto> permissionDtoSet = new HashSet<>() {{
            add(new PermissionDto("MIN", AccessLevel.READ));
        }};
        teamService.deleteTeamPermissions(team.getUuid(), permissionDtoSet);

        verify(auditClient, times(1)).deleteTeamPermissionsAudit(permissionDtoSet);

    }

    @Test
    public void ShouldAuditSuccessfulDeleteTeam() {
        Team team = new Team("a team", true);
        when(parentTopicRepository.findAllActiveParentTopicsForTeam(team.getUuid())).thenReturn(new ArrayList<>());
        when(teamRepository.findByUuid(team.getUuid())).thenReturn(team);

        assertThat(team.isActive()).isTrue();
        teamService.deleteTeam(team.getUuid());
        assertThat(team.isActive()).isFalse();

        verify(auditClient, times(1)).deleteTeamAudit(team);

    }

    @Test
    public void shouldNotAuditWhenDeleteTeamThrowsException() {
        List<ParentTopic> parentTopicsList = new ArrayList<>();
        parentTopicsList.add(new ParentTopic());
        when(parentTopicRepository.findAllActiveParentTopicsForTeam(team1UUID)).thenReturn(parentTopicsList);
        try {
            teamService.deleteTeam(team1UUID);
        } catch (ApplicationExceptions.TeamDeleteException e) {
            // do nothing
        }
        verifyNoMoreInteractions(auditClient);
    }

    @Test
    public void shouldGetAllFirstDescendantTeamsFromCurrentTeam() {
        UUID teamUuid = UUID.randomUUID();
        Set<Team> teams = Set.of(new Team(UUID.randomUUID().toString(), true));

        when(teamRepository.findAllActiveFirstDescendantTeamsFromAscendant(teamUuid)).thenReturn(teams);

        Set<Team> expectedTeams = teamService.getAllFirstDescendantTeams(teamUuid);

        assertThat(expectedTeams.iterator().next().getUuid()).isEqualTo(teams.iterator().next().getUuid());
        verify(teamRepository).findAllActiveFirstDescendantTeamsFromAscendant(teamUuid);
        verifyNoMoreInteractions(caseworkClient, teamRepository);
    }

    private List<Team> getAllTeams() {
        CaseType caseType = new CaseType(1L, UUID.randomUUID(), "MIN", "a1", "MIN", UUID.randomUUID(),
            "DCU_MIN_DISPATCH", true, true, null, null);
        Set<Permission> permissions = new HashSet<Permission>() {{
            add(new Permission(AccessLevel.OWNER, null, caseType));
            add(new Permission(AccessLevel.OWNER, null, caseType));
        }};

        return new ArrayList<Team>() {{
            add(new Team("Team1", permissions));
            add(new Team("Team2", false));
            add(new Team("Team3", true));
        }};
    }

    private List<Team> getTeams() {
        CaseType caseType = new CaseType(1L, UUID.randomUUID(), "MIN", "a1", "MIN", UUID.randomUUID(),
            "DCU_MIN_DISPATCH", true, true, null, null);
        Set<Permission> permissions = new HashSet<Permission>() {{
            add(new Permission(AccessLevel.OWNER, null, caseType));
            add(new Permission(AccessLevel.OWNER, null, caseType));
        }};

        return new ArrayList<Team>() {{
            add(new Team("Team1", permissions));
            add(new Team("Team2", new HashSet<>()));
        }};
    }

    @Test
    public void shouldAuditSuccessfulRemoveUserFromTeam() {
        Team team = new Team("a team", true);
        UUID userUUID = UUID.randomUUID();
        UUID teamUUID = UUID.randomUUID();

        when(caseworkClient.getCasesForUser(userUUID, teamUUID)).thenReturn(new HashSet<>());

        teamService.removeUserFromTeam(userUUID, teamUUID);
        verify(auditClient, times(1)).removeUserFromTeamAudit(userUUID, teamUUID);
    }

    @Test
    public void shouldNotAuditWhenRemoveUserFromTeamThrowsException() {
        Team team = new Team("a team", true);
        UUID userUUID = UUID.randomUUID();
        UUID teamUUID = UUID.randomUUID();

        Set<UUID> casesSet = new HashSet<>();
        casesSet.add(UUID.randomUUID());
        when(caseworkClient.getCasesForUser(userUUID, teamUUID)).thenReturn(casesSet);

        try {
            teamService.removeUserFromTeam(userUUID, teamUUID);
        } catch (ApplicationExceptions.UserRemoveException e) {
            //Do nothing
        }

        verifyNoMoreInteractions(auditClient);
    }

    @Test
    public void shouldNotPatchTeam_WhenValuesUnchanged() {
        String teamName = "__team_name__";
        Unit teamUnit = new Unit("Unit", "Unit", true);

        PatchTeamDto request = new PatchTeamDto();
        request.setUnitUuid(teamUnit.getUuid());
        request.setDisplayName(teamName);

        Team team = new Team(teamName, true);
        team.setUnit(teamUnit);
        when(teamRepository.findByUuid(team.getUuid())).thenReturn(team);

        teamService.patchTeam(team.getUuid(), request);

        verify(teamRepository, times(1)).findByUuid(team.getUuid());

        verifyNoMoreInteractions(auditClient);
        verifyNoMoreInteractions(teamRepository);
    }

    @Test
    public void shouldNotPatchTeam_WhenUnchangedActiveValueGiven() {
        UUID teamUuid = UUID.randomUUID();
        PatchTeamDto request = new PatchTeamDto();
        request.setActive(true);

        Team team = mock(Team.class);

        when(team.isActive()).thenReturn(true);
        when(teamRepository.findByUuid(teamUuid)).thenReturn(team);

        teamService.patchTeam(teamUuid, request);

        verify(teamRepository, times(1)).findByUuid(teamUuid);
        verify(team, times(1)).isActive();

        verifyNoMoreInteractions(auditClient);
        verifyNoMoreInteractions(teamRepository);
        verifyNoMoreInteractions(team);
    }

    @Test
    public void shouldThrow_WhenAnNullActiveFlagProvided() {
        UUID teamUuid = UUID.randomUUID();
        PatchTeamDto request = new PatchTeamDto();
        request.setActive(true);

        Team team = mock(Team.class);

        when(team.isActive()).thenReturn(true);
        when(teamRepository.findByUuid(teamUuid)).thenReturn(team);

        teamService.patchTeam(teamUuid, request);

        verify(teamRepository, times(1)).findByUuid(teamUuid);

        verify(team, times(1)).isActive();

        verifyNoMoreInteractions(auditClient);
        verifyNoMoreInteractions(teamRepository);
        verifyNoMoreInteractions(team);
    }

    @Test
    public void shouldPatchTeam_UpdateActiveFlagWhenNewActiveValueSet() {
        UUID teamUuid = UUID.randomUUID();
        PatchTeamDto request = new PatchTeamDto();
        request.setActive(false);

        Team team = mock(Team.class);

        when(team.getUuid()).thenReturn(teamUuid);
        when(team.isActive()).thenReturn(true);

        when(teamRepository.findByUuid(teamUuid)).thenReturn(team);

        teamService.patchTeam(teamUuid, request);

        verify(teamRepository, times(2)).findByUuid(teamUuid);
        verify(auditClient, times(1)).setTeamActivationFlag(team);

        verify(team, times(1)).getUuid();
        verify(team, times(1)).isActive();
        verify(team, times(1)).setActive(false);

        verifyNoMoreInteractions(auditClient);
        verifyNoMoreInteractions(teamRepository);
        verifyNoMoreInteractions(team);
    }

    @Test
    public void shouldPatchTeam_UpdateNameWhenNewNameGiven() {
        UUID teamUuid = UUID.randomUUID();
        PatchTeamDto request = new PatchTeamDto();
        String newTeamName = "__new_team_name__";
        request.setDisplayName(newTeamName);

        Team team = mock(Team.class);

        when(team.getDisplayName()).thenReturn("__old_team_name__");
        when(team.getUuid()).thenReturn(teamUuid);

        when(teamRepository.findByUuid(teamUuid)).thenReturn(team);
        when(teamRepository.findByDisplayName(newTeamName)).thenReturn(null);

        teamService.patchTeam(teamUuid, request);

        verify(teamRepository, times(2)).findByUuid(teamUuid);
        verify(teamRepository, times(1)).findByDisplayName(newTeamName);
        verify(auditClient, times(1)).renameTeamAudit(team);

        verify(team, times(1)).getUuid();
        verify(team, times(2)).getDisplayName();
        verify(team, times(1)).setDisplayName(newTeamName);

        verifyNoMoreInteractions(auditClient);
        verifyNoMoreInteractions(teamRepository);
        verifyNoMoreInteractions(team);
    }

    @Test
    public void shouldPatchTeam_UpdateUnitWhenUnitGiven() {
        UUID teamUuid = UUID.randomUUID();
        UUID newUnitUuid = UUID.randomUUID();
        UUID oldUnitUuid = UUID.randomUUID();

        PatchTeamDto request = new PatchTeamDto();
        request.setUnitUuid(newUnitUuid);

        Team team = mock(Team.class);
        Unit newUnit = mock(Unit.class);
        Unit oldUnit = mock(Unit.class);

        when(oldUnit.getUuid()).thenReturn(oldUnitUuid);
        when(oldUnit.getShortCode()).thenReturn("UNIT1");
        when(newUnit.getShortCode()).thenReturn("UNIT2");

        when(team.getUnit()).thenReturn(oldUnit);

        when(unitRepository.findByUuid(newUnitUuid)).thenReturn(newUnit);
        when(unitRepository.findByUuid(oldUnitUuid)).thenReturn(oldUnit);

        when(teamRepository.findByUuid(teamUuid)).thenReturn(team);

        teamService.patchTeam(teamUuid, request);

        verify(teamRepository, times(2)).findByUuid(teamUuid);
        verify(auditClient, times(1)).moveToNewUnitAudit(teamUuid.toString(), "UNIT1", "UNIT2");

        verify(team, times(2)).getUnit();

        verify(unitRepository, times(1)).findByUuid(newUnitUuid);
        verify(unitRepository, times(1)).findByUuid(oldUnitUuid);

        verify(newUnit, times(1)).addTeam(team);
        verify(oldUnit, times(1)).removeTeam(teamUuid);

        verifyNoMoreInteractions(auditClient, teamRepository, team);
    }

}
