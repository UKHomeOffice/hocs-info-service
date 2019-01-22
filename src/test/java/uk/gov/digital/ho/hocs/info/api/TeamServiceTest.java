package uk.gov.digital.ho.hocs.info.api;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.digital.ho.hocs.info.api.dto.PermissionDto;
import uk.gov.digital.ho.hocs.info.api.dto.TeamDto;
import uk.gov.digital.ho.hocs.info.client.auditClient.AuditClient;
import uk.gov.digital.ho.hocs.info.client.caseworkclient.CaseworkClient;
import uk.gov.digital.ho.hocs.info.domain.exception.ApplicationExceptions;
import uk.gov.digital.ho.hocs.info.domain.model.*;
import uk.gov.digital.ho.hocs.info.domain.repository.ParentTopicRepository;
import uk.gov.digital.ho.hocs.info.domain.repository.TeamRepository;
import uk.gov.digital.ho.hocs.info.domain.repository.UnitRepository;
import uk.gov.digital.ho.hocs.info.security.AccessLevel;
import uk.gov.digital.ho.hocs.info.security.Base64UUID;
import uk.gov.digital.ho.hocs.info.security.KeycloakService;

import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

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

    private UUID team1UUID =UUID.randomUUID();
    private String base64Team1UUID = Base64UUID.UUIDToBase64String(team1UUID);

    private UUID team2UUID =UUID.randomUUID();



    @Test
    public void shouldGetAllTeams() {

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
        Team team = new Team( "Team1", team1UUID, new HashSet<>());
        when(teamRepository.findByUuid(team1UUID)).thenReturn(team);
        teamService.getTeam(team1UUID);
        verify(teamRepository, times(1)).findByUuid(team1UUID);
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
    public void shouldAddTeamToRepositoryAndKeycloak() {

        UUID unitUUID = UUID.randomUUID();
        Team team = new Team("Team1", team1UUID, true);
        Unit unit = new Unit("UNIT1", "UNIT1", unitUUID,true);
        unit.addTeam(team);

        TeamDto teamDto = new TeamDto( "Team1", team1UUID, true, new HashSet<>());

        when(teamRepository.findByUuid(team1UUID)).thenReturn(null);
        when(unitRepository.findByUuid(unitUUID)).thenReturn(unit);

        Team result = teamService.createTeam(teamDto, unitUUID);

        assertThat(result.getUuid()).isEqualTo(team1UUID);
        verify(teamRepository, times(1)).findByUuid(team1UUID);
        verify(unitRepository, times(1)).findByUuid(unitUUID);
        verify(keycloakService, times(1)).createTeamGroupIfNotExists(base64Team1UUID);
        verifyNoMoreInteractions(teamRepository);
        verifyNoMoreInteractions(keycloakService);
    }

    @Test
    public void shouldAddTeamToKeycloakIfTeamExists() {

        UUID unitUUID = UUID.randomUUID();
        Team team = new Team("Team1", team1UUID, true);
        Unit unit = new Unit("UNIT1", "UNIT1", unitUUID,true);
        unit.addTeam(team);

        TeamDto teamDto = new TeamDto( "Team1", team1UUID, true, new HashSet<>());
        when(unitRepository.findByUuid(unitUUID)).thenReturn(unit);
        when(teamRepository.findByUuid(team1UUID)).thenReturn(team);

        teamService.createTeam(teamDto, unitUUID);
        verify(unitRepository, times(1)).findByUuid(unitUUID);
        verify(unitRepository, never()).save(unit);
        verify(keycloakService, times(1)).createTeamGroupIfNotExists(base64Team1UUID);
        verifyNoMoreInteractions(keycloakService);
    }

    @Test
    public void shouldMarkTeamAsInactive() {
        Team team = new Team( "Team1", team1UUID, true);
        when(teamRepository.findByUuid(team1UUID)).thenReturn(team);
        assertThat(team.isActive()).isTrue();
        teamService.deleteTeam(team1UUID);
        assertThat(team.isActive()).isFalse();
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

        UUID unitUUID = UUID.randomUUID();
        UUID userUUID = UUID.randomUUID();

        Set<Permission> permissions = new HashSet<>();
        Unit unit = new Unit("a unit", "UNIT", unitUUID, true);
        CaseType caseType = new CaseType(1L, UUID.randomUUID(), "MIN","a1", "MIN", UUID.randomUUID(), "DCU_MIN_DISPATCH", true, true);
        Permission permission = new Permission(AccessLevel.OWNER, null, caseType);
        permissions.add(permission);
        Team team = new Team("a team", team1UUID, true);
        team.setUnit(unit);
        team.addPermissions(permissions);

        String teamGroupPath =  "/" + base64Team1UUID;
        String caseGroupPath =  teamGroupPath + "/MIN";
        String accessLevelGroupPath = teamGroupPath + "/MIN/5";

        when(teamRepository.findByUuid(team1UUID)).thenReturn(team);
        teamService.addUserToTeam(userUUID, team1UUID);
        verify(keycloakService, times(1)).createTeamGroupIfNotExists(base64Team1UUID);
        verify(keycloakService, times(1)).createGroupPathIfNotExists(teamGroupPath, "MIN");
        verify(keycloakService, times(1)).createGroupPathIfNotExists(caseGroupPath, "5");
        verify(keycloakService, times(1)).addUserToGroup(userUUID, accessLevelGroupPath);
    }

    @Test
    public void shouldUpdatePermissionsInDatabaseAndKeycloak() {

        UUID unitUUID = UUID.randomUUID();
        Unit unit = new Unit("a unit", "UNIT", unitUUID, true);
        Team team = new Team("a team", team1UUID, true);
        team.setUnit(unit);

        CaseType caseType = new CaseType(1L, UUID.randomUUID(), "MIN","a1", "MIN", UUID.randomUUID(), "DCU_MIN_DISPATCH", true, true);

        when(teamRepository.findByUuid(team1UUID)).thenReturn(team);
        when(caseTypeService.getCaseType(any())).thenReturn(caseType);

        Set<PermissionDto> permissions = new HashSet<PermissionDto>() {{
            add(new PermissionDto("MIN", AccessLevel.READ));
            add(new PermissionDto("MIN", AccessLevel.OWNER));
        }};

        Set<String> permissionPaths = new HashSet<String>() {{
            add("/" + base64Team1UUID + "/MIN/2");
            add("/" + base64Team1UUID + "/MIN/5");
        }};

        assertThat(team.getPermissions().size()).isEqualTo(0);
        teamService.updateTeamPermissions(team1UUID, permissions);
        assertThat(team.getPermissions().size()).isEqualTo(2);


        verify(teamRepository, times(1)).findByUuid(team1UUID);
        verify(keycloakService, times(1)).updateUserTeamGroups(team1UUID,permissionPaths);
        verifyNoMoreInteractions(teamRepository);
    }

    @Test
    public void shouldDeleteSinglePermissionInDatabaseAndKeycloak() {

        UUID unitUUID = UUID.randomUUID();
        Set<Permission> permissions = new HashSet<>();
        Unit unit = new Unit("a unit", "UNIT", unitUUID, true);
        Team team = new Team("a team", team1UUID, true);
        team.setUnit(unit);
        CaseType caseType = new CaseType(1L,UUID.randomUUID(), "MIN","a1", "MIN", UUID.randomUUID(), "DCU_MIN_DISPATCH", true,true);
        permissions.add(new Permission(AccessLevel.READ, team, caseType));
        permissions.add(new Permission(AccessLevel.OWNER, team, caseType));
        team.addPermissions(permissions);

        when(teamRepository.findByUuid(team1UUID)).thenReturn(team);
        when(caseTypeService.getCaseType(any())).thenReturn(caseType);
        doNothing().when(keycloakService).deleteTeamPermisisons(any());

        Set<PermissionDto> permissionDtoSet = new HashSet<PermissionDto>() {{
            add(new PermissionDto("MIN", AccessLevel.READ));
        }};

        assertThat(team.getPermissions().size()).isEqualTo(2);
        teamService.deleteTeamPermissions(team1UUID, permissionDtoSet);
        assertThat(team.getPermissions().size()).isEqualTo(1);


        verify(teamRepository, times(1)).findByUuid(team1UUID);
        verify(keycloakService, times(1)).deleteTeamPermisisons("/"+ base64Team1UUID+"/MIN/2");
        verifyNoMoreInteractions(teamRepository);
    }

    @Test
    public void shouldDeleteMultiplePermissionInDatabaseAndKeycloak() {

        UUID unitUUID = UUID.randomUUID();
        Set<Permission> permissions = new HashSet<>();
        Unit unit = new Unit( "a unit", "UNIT", unitUUID, true);
        Team team = new Team("a team", team1UUID, true);
        team.setUnit(unit);
        CaseType caseType = new CaseType(1L, UUID.randomUUID(), "MIN","a1", "MIN", UUID.randomUUID(), "DCU_MIN_DISPATCH", true, true);
        permissions.add(new Permission(AccessLevel.READ, team, caseType));
        permissions.add(new Permission(AccessLevel.OWNER, team, caseType));
        team.addPermissions(permissions);

        when(teamRepository.findByUuid(team1UUID)).thenReturn(team);
        when(caseTypeService.getCaseType(any())).thenReturn(caseType);
        doNothing().when(keycloakService).deleteTeamPermisisons(any());

        Set<PermissionDto> permissionDtoSet = new HashSet<PermissionDto>() {{
            add(new PermissionDto("MIN", AccessLevel.READ));
            add(new PermissionDto("MIN", AccessLevel.OWNER));
        }};

        assertThat(team.getPermissions().size()).isEqualTo(2);
        teamService.deleteTeamPermissions(team1UUID, permissionDtoSet);
        assertThat(team.getPermissions().size()).isEqualTo(0);


        verify(teamRepository, times(1)).findByUuid(team1UUID);
        verify(keycloakService, times(1)).deleteTeamPermisisons("/" + base64Team1UUID+"/MIN/2");
        verify(keycloakService, times(1)).deleteTeamPermisisons("/" + base64Team1UUID+"/MIN/5");
        verifyNoMoreInteractions(teamRepository);
    }

    @Test
    public void ShouldDeleteTeamWhenNoActiveParentTopicsAreLinkedToIt(){
        Team team = new Team("a team", team1UUID, true);
        when(parentTopicRepository.findAllActiveParentTopicsForTeam(team1UUID)).thenReturn(new ArrayList<>());
        when(teamRepository.findByUuid(team1UUID)).thenReturn(team);

        assertThat(team.isActive()).isTrue();
        teamService.deleteTeam(team1UUID);
        assertThat(team.isActive()).isFalse();

        verify(parentTopicRepository, times(1)).findAllActiveParentTopicsForTeam(team1UUID);
        verify(teamRepository, times(1)).findByUuid(team1UUID);
        verifyNoMoreInteractions(parentTopicRepository);
        verifyNoMoreInteractions(teamRepository);
    }

    @Test(expected = ApplicationExceptions.TeamDeleteException.class)
    public void shouldThrowTeamDeleteExceptionWhenActiveParentTopicsAreLinkedToTeam(){
        List<ParentTopic> parentTopicsList = new ArrayList<>();
        parentTopicsList.add(new ParentTopic());
        when(parentTopicRepository.findAllActiveParentTopicsForTeam(team1UUID)).thenReturn(parentTopicsList);

        teamService.deleteTeam(team1UUID);
    }

    @Test
    public void ShouldAuditCreateTeam(){
        UUID unitUUID = UUID.randomUUID();
        Team team = new Team( "Team1", team1UUID, true);
        Unit unit = new Unit("UNIT1", "UNIT1", unitUUID,true);
        unit.addTeam(team);
        TeamDto teamDto = new TeamDto( "Team1", team1UUID, true, new HashSet<>());

        when(unitRepository.findByUuid(unitUUID)).thenReturn(unit);
        teamService.createTeam(teamDto, unitUUID);

        verify(auditClient, times(1)).createTeamAudit(team);
    }

    @Test
    public void ShouldAuditUpdateTeamName(){
        Team team = new Team( "Team1", team1UUID, true);

        when(teamRepository.findByUuid(team1UUID)).thenReturn(team);
        teamService.updateTeamName(team1UUID,"" );

        verify(auditClient, times(1)).renameTeamAudit(team);
    }


    @Test
    public void ShouldAuditAddUserToTeam(){
        UUID userUUID = UUID.randomUUID();

        Set<Permission> permissions = new HashSet<>();
        Unit unit = new Unit("a unit", "UNIT", UUID.randomUUID(), true);
        CaseType caseType = new CaseType(1L, UUID.randomUUID(), "MIN","a1", "MIN", UUID.randomUUID(), "DCU_MIN_DISPATCH", true, true);
        Permission permission = new Permission(AccessLevel.OWNER, null, caseType);
        permissions.add(permission);
        Team team = new Team("a team", team1UUID, true);
        team.setUnit(unit);

        when(teamRepository.findByUuid(team1UUID)).thenReturn(team);
        teamService.addUserToTeam(userUUID, team1UUID);
        verify(auditClient, times(1)).addUserToTeamAudit(userUUID, team);
    }

    @Test
    public void ShouldAuditMoveToNewUnit(){
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

        verify(auditClient, times(1)).moveToNewUnitAudit(team1UUID.toString(), "UNIT1" , "UNIT2");

    }

    @Test
    public void ShouldAuditUpdateTeamPermissions(){
        UUID unitUUID = UUID.randomUUID();
        Unit unit = new Unit("a unit", "UNIT", unitUUID,true);
        Team team = new Team("a team", team1UUID, true);
        team.setUnit(unit);

        CaseType caseType = new CaseType(1L, UUID.randomUUID(), "MIN","a1", "MIN", UUID.randomUUID(), "DCU_MIN_DISPATCH", true, true);

        when(teamRepository.findByUuid(team1UUID)).thenReturn(team);
        when(caseTypeService.getCaseType(any())).thenReturn(caseType);

        Set<PermissionDto> permissions = new HashSet<PermissionDto>() {{
            add(new PermissionDto("MIN", AccessLevel.READ));
            add(new PermissionDto("MIN", AccessLevel.OWNER));
        }};

        Set<String> permissionPaths = new HashSet<String>() {{
            add("/" + team1UUID + "/MIN/2");
            add("/" + team1UUID + "/MIN/5");
        }};

        teamService.updateTeamPermissions(team1UUID, permissions);

        verify(auditClient, times(1)).updateTeamPermissionsAudit(team1UUID, permissions);
    }

    @Test
    public void ShouldAuditDeleteTeamPermissions(){
        UUID unitUUID = UUID.randomUUID();
        Set<Permission> permissions = new HashSet<>();
        Unit unit = new Unit( "a unit", "UNIT", unitUUID, true);
        Team team = new Team("a team", team1UUID, true);
        team.addPermissions(permissions);
        team.setUnit(unit);
        CaseType caseType = new CaseType(1L, UUID.randomUUID(), "MIN","a1", "MIN", UUID.randomUUID(), "DCU_MIN_DISPATCH", true, true);
        permissions.add(new Permission(AccessLevel.READ, team, caseType));
        permissions.add(new Permission(AccessLevel.OWNER, team, caseType));

        when(teamRepository.findByUuid(team1UUID)).thenReturn(team);
        when(caseTypeService.getCaseType(any())).thenReturn(caseType);
        doNothing().when(keycloakService).deleteTeamPermisisons(any());

        Set<PermissionDto> permissionDtoSet = new HashSet<PermissionDto>() {{
            add(new PermissionDto("MIN", AccessLevel.READ));
        }};
        teamService.deleteTeamPermissions(team1UUID, permissionDtoSet);

        verify(auditClient, times(1)).deleteTeamPermissionsAudit(team1UUID, permissionDtoSet);

    }

    @Test
    public void ShouldAuditSuccessfulDeleteTeam(){
        Team team = new Team("a team", team1UUID, true);
        when(parentTopicRepository.findAllActiveParentTopicsForTeam(team1UUID)).thenReturn(new ArrayList<>());
        when(teamRepository.findByUuid(team1UUID)).thenReturn(team);

        assertThat(team.isActive()).isTrue();
        teamService.deleteTeam(team1UUID);
        assertThat(team.isActive()).isFalse();

        verify(auditClient, times(1)).deleteTeamAudit(team);

    }

    @Test
    public void shouldNotAuditWhenDeleteTeamThrowsException(){
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

    private List<Team> getTeams() {
        CaseType caseType = new CaseType(1L,UUID.randomUUID(), "MIN","a1", "MIN", UUID.randomUUID(),"DCU_MIN_DISPATCH", true, true);
        Set<Permission> permissions = new HashSet<Permission>() {{
            add(new Permission( AccessLevel.OWNER, null, caseType));
            add(new Permission( AccessLevel.OWNER, null, caseType));
        }};

        return new ArrayList<Team>() {{
            add(new Team("Team1", team1UUID,permissions));
            add(new Team( "Team2", team2UUID, new HashSet<>()));
        }};
    }
}