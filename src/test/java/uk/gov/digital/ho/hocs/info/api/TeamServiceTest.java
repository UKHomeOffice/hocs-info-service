package uk.gov.digital.ho.hocs.info.api;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.keycloak.admin.client.Keycloak;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.digital.ho.hocs.info.api.dto.PermissionDto;
import uk.gov.digital.ho.hocs.info.api.dto.TeamDto;
import uk.gov.digital.ho.hocs.info.client.audit.client.AuditClient;
import uk.gov.digital.ho.hocs.info.client.caseworkclient.CaseworkClient;
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

    private static final String HOCS_REALM = "hocs";
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
    private Keycloak keycloakClient;

    @Mock
    private AuditClient auditClient;

    @Mock
    private CaseworkClient caseworkClient;

    private TeamService teamService;

    @Before
    public void setUp() {
        this.teamService = new TeamService(
                teamRepository,
                unitRepository,
                caseTypeService,
                parentTopicRepository,
                keycloakService,
                auditClient,
                caseworkClient);
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
        Team team = mock(Team.class);
        when(teamRepository.findByUuid(team1UUID)).thenReturn(team);
        when(team.getUuid()).thenReturn(team1UUID);
        teamService.updateTeamName(team1UUID, "new team name");

        verify(teamRepository, times(1)).findByUuid(team1UUID);
        verify(team, times(1)).setDisplayName("new team name");
        verifyNoMoreInteractions(teamRepository);
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

        when(teamRepository.findByUuid(any())).thenReturn(null);
        when(unitRepository.findByUuid(unit.getUuid())).thenReturn(unit);

        Team result = teamService.createTeam(teamDto, unit.getUuid());

        verify(teamRepository, times(1)).findByUuid(any());
        verify(unitRepository, times(1)).findByUuid(unit.getUuid());
        verify(keycloakService, times(1)).createTeamGroupIfNotExists(result.getUuid());
        verifyNoMoreInteractions(teamRepository);
        verifyNoMoreInteractions(keycloakService);
    }

    @Test
    public void shouldAddTeamToKeycloakIfTeamExists() {

        Team team = new Team("Team1", true);
        Unit unit = new Unit("UNIT1", "UNIT1", true);
        unit.addTeam(team);

        TeamDto teamDto = new TeamDto("Team1", null, team1UUID, true, new HashSet<>(), null);
        when(unitRepository.findByUuid(unit.getUuid())).thenReturn(unit);
        when(teamRepository.findByUuid(team1UUID)).thenReturn(team);

        teamService.createTeam(teamDto, unit.getUuid());
        verify(unitRepository, times(1)).findByUuid(unit.getUuid());
        verify(unitRepository, never()).save(unit);
        verify(keycloakService, times(1)).createTeamGroupIfNotExists(team.getUuid());
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
    public void shouldAddUserToTeam() {
        UUID userUUID = UUID.randomUUID();

        Set<Permission> permissions = new HashSet<>();
        Unit unit = new Unit("a unit", "UNIT", true);
        CaseType caseType = new CaseType(1L, UUID.randomUUID(), "MIN", "a1", "MIN", UUID.randomUUID(), "DCU_MIN_DISPATCH", true, true);
        Permission permission = new Permission(AccessLevel.OWNER, null, caseType);
        permissions.add(permission);
        Team team = new Team("a team", true);
        team.setUnit(unit);
        team.addPermissions(permissions);

        when(teamRepository.findByUuid(team.getUuid())).thenReturn(team);
        teamService.addUserToTeam(userUUID, team.getUuid());
        verify(keycloakService, times(1)).createTeamGroupIfNotExists(team.getUuid());
        verify(keycloakService, times(1)).addUserToTeam(userUUID, team.getUuid());
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

        verifyZeroInteractions(keycloakService);
    }

    @Test
    public void shouldUpdatePermissionsInDatabase() {

        Unit unit = new Unit("a unit", "UNIT", true);
        Team team = new Team("a team", true);
        team.setUnit(unit);

        CaseType caseType = new CaseType(1L, UUID.randomUUID(), "MIN", "a1", "MIN", UUID.randomUUID(), "DCU_MIN_DISPATCH", true, true);

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
        CaseType caseType = new CaseType(1L, UUID.randomUUID(), "MIN", "a1", "MIN", UUID.randomUUID(), "DCU_MIN_DISPATCH", true, true);
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
        CaseType caseType = new CaseType(1L, UUID.randomUUID(), "MIN", "a1", "MIN", UUID.randomUUID(), "DCU_MIN_DISPATCH", true, true);
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
        UUID userUUID = UUID.randomUUID();

        Set<Permission> permissions = new HashSet<>();
        Unit unit = new Unit("a unit", "UNIT", true);
        CaseType caseType = new CaseType(1L, UUID.randomUUID(), "MIN", "a1", "MIN", UUID.randomUUID(), "DCU_MIN_DISPATCH", true, true);
        Permission permission = new Permission(AccessLevel.OWNER, null, caseType);
        permissions.add(permission);
        Team team = new Team("a team", true);
        team.setUnit(unit);

        when(teamRepository.findByUuid(team.getUuid())).thenReturn(team);
        teamService.addUserToTeam(userUUID, team.getUuid());
        verify(auditClient, times(1)).addUserToTeamAudit(userUUID, team);
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
    public void ShouldAuditUpdateTeamPermissions() {
        Unit unit = new Unit("a unit", "UNIT", true);
        Team team = new Team("a team", true);
        team.setUnit(unit);

        CaseType caseType = new CaseType(1L, UUID.randomUUID(), "MIN", "a1", "MIN", UUID.randomUUID(), "DCU_MIN_DISPATCH", true, true);

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
        CaseType caseType = new CaseType(1L, UUID.randomUUID(), "MIN", "a1", "MIN", UUID.randomUUID(), "DCU_MIN_DISPATCH", true, true);
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
        verifyZeroInteractions(auditClient);
    }

    private List<Team> getAllTeams() {
        CaseType caseType = new CaseType(1L, UUID.randomUUID(), "MIN", "a1", "MIN", UUID.randomUUID(), "DCU_MIN_DISPATCH", true, true);
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
        CaseType caseType = new CaseType(1L, UUID.randomUUID(), "MIN", "a1", "MIN", UUID.randomUUID(), "DCU_MIN_DISPATCH", true, true);
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

        verifyZeroInteractions(auditClient);
    }


}
