package uk.gov.digital.ho.hocs.info.team;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.digital.ho.hocs.info.dto.PermissionDto;
import uk.gov.digital.ho.hocs.info.dto.TeamDto;
import uk.gov.digital.ho.hocs.info.entities.CaseTypeEntity;
import uk.gov.digital.ho.hocs.info.entities.Permission;
import uk.gov.digital.ho.hocs.info.entities.Team;
import uk.gov.digital.ho.hocs.info.entities.Unit;
import uk.gov.digital.ho.hocs.info.exception.EntityNotFoundException;
import uk.gov.digital.ho.hocs.info.repositories.CaseTypeRepository;
import uk.gov.digital.ho.hocs.info.repositories.TeamRepository;
import uk.gov.digital.ho.hocs.info.repositories.UnitRepository;
import uk.gov.digital.ho.hocs.info.security.AccessLevel;
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
    private CaseTypeRepository caseTypeRepository;

    @Mock
    private KeycloakService keycloakService;

    private TeamService teamService;

    @Before
    public void setUp() {
        this.teamService = new TeamService(teamRepository, unitRepository, caseTypeRepository, keycloakService);
    }

    private UUID team1UUID =UUID.randomUUID();
    private UUID team2UUID =UUID.randomUUID();


    @Test
    public void shouldGetAllTeams() {

        when(teamRepository.findAllByActiveTrue()).thenReturn(getTeams().stream().collect(Collectors.toSet()));
        Set<TeamDto> result = teamService.getAllActiveTeams();
        assertThat(result).size().isEqualTo(2);
        verify(teamRepository, times(1)).findAllByActiveTrue();
        verifyNoMoreInteractions(teamRepository);
    }

    @Test
    public void shouldGetAllTeamsForUnit() {
        UUID unitUUID = UUID.randomUUID();
        when(teamRepository.findTeamsByUnitUuid(unitUUID)).thenReturn(getTeams().stream().collect(Collectors.toSet()));
        Set<TeamDto> result = teamService.getTeamsForUnit(unitUUID);
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

    @Test(expected = EntityNotFoundException.class)
    public void throwExceptionWhenTeamDoesNotExist() {
        when(teamRepository.findByUuid(team1UUID)).thenThrow(new EntityNotFoundException(""));
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
        Team team = new Team( "Team1", team1UUID, true);
        Unit unit = new Unit(1L,"UNIT1", "UNIT1", unitUUID,true,
                new HashSet<Team>(){{
                add(team);
                }});

        TeamDto teamDto = new TeamDto( "Team1", team1UUID, true, new HashSet<>());

        when(teamRepository.findByUuid(team1UUID)).thenReturn(null);
        when(unitRepository.findByUuid(unitUUID)).thenReturn(unit);

        TeamDto result = teamService.createTeam(teamDto, unitUUID);

        assertThat(result.getUuid()).isEqualTo(team1UUID);
        verify(teamRepository, times(1)).findByUuid(team1UUID);
        verify(unitRepository, times(1)).findByUuid(unitUUID);
        verify(keycloakService, times(1)).createUnitGroupIfNotExists("UNIT1");
        verify(keycloakService, times(1)).createGroupPathIfNotExists("UNIT1", team1UUID.toString());
        verifyNoMoreInteractions(teamRepository);
        verifyNoMoreInteractions(keycloakService);
    }

    @Test
    public void shouldAddTeamToKeycloakIfTeamExists() {

        UUID unitUUID = UUID.randomUUID();
        Team team = new Team( "Team1", team1UUID, true);
        Unit unit = new Unit(1L,"UNIT1", "UNIT1", unitUUID,true,
                new HashSet<Team>(){{
                    add(team);
                }});

        TeamDto teamDto = new TeamDto( "Team1", team1UUID, true, new HashSet<>());
        when(unitRepository.findByUuid(unitUUID)).thenReturn(unit);
        when(teamRepository.findByUuid(team1UUID)).thenReturn(team);

        teamService.createTeam(teamDto, unitUUID);
        verify(unitRepository, times(1)).findByUuid(unitUUID);
        verify(unitRepository, never()).save(unit);
        verify(keycloakService, times(1)).createUnitGroupIfNotExists("UNIT1");
        verify(keycloakService, times(1)).createGroupPathIfNotExists("UNIT1", team1UUID.toString());
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
        TeamDto result = teamService.getTeam(team1UUID);
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
        when(team.getUuid()).thenReturn(team1UUID);
        when(team.getUnit().getShortCode()).thenReturn("UNIT1");

        teamService.moveToNewUnit(unitUUID, team1UUID);

        verify(unit, times(1)).addTeam(team);
        verify(oldUnit, times(1)).removeTeam(team1UUID);
        verify(keycloakService, times(1)).moveGroup("/UNIT1/" + team1UUID.toString(), "UNIT1");
    }


    @Test
    public void shouldAddUserToTeam() {

        UUID unitUUID = UUID.randomUUID();
        UUID userUUID = UUID.randomUUID();

        Set<Permission> permissions = new HashSet<>();
        Unit unit = new Unit(1L, "a unit", "UNIT", unitUUID, true, new HashSet<>());
        CaseTypeEntity caseType = new CaseTypeEntity(1L, "MIN","a1", "MIN", "ROLE", "DCU_MIN_DISPATCH", true);
        Permission permission = new Permission(1L, AccessLevel.OWNER, null, caseType);
        permissions.add(permission);
        Team team = new Team(1L, "a team", team1UUID, true, unit, permissions);

        String unitGroupPath = "UNIT";
        String teamGroupPath = "/UNIT/" + team1UUID.toString();
        String caseGroupPath = "/UNIT/" + team1UUID.toString() + "/MIN";
        String accessLevelGroupPath = "/UNIT/" + team1UUID.toString() + "/MIN/OWNER";

        when(teamRepository.findByUuid(team1UUID)).thenReturn(team);

        teamService = new TeamService(teamRepository, unitRepository, caseTypeRepository, keycloakService);
        teamService.addUserToTeam(userUUID, team1UUID);

        verify(keycloakService, times(1)).createGroupPathIfNotExists(unitGroupPath, team1UUID.toString());
        verify(keycloakService, times(1)).createGroupPathIfNotExists(teamGroupPath, "MIN");
        verify(keycloakService, times(1)).createGroupPathIfNotExists(caseGroupPath, "OWNER");
        verify(keycloakService, times(1)).addUserToGroup(userUUID, teamGroupPath);
        verify(keycloakService, times(1)).addUserToGroup(userUUID, accessLevelGroupPath);
    }

    @Test
    public void shouldUpdatePermissionsInDatabaseAndKeycloak() {

        UUID unitUUID = UUID.randomUUID();
        Unit unit = new Unit(1L, "a unit", "UNIT", unitUUID, true, new HashSet<>());
        Team team = new Team(1L, "a team", team1UUID, true, unit, new HashSet<>());

        CaseTypeEntity caseType = new CaseTypeEntity(1L, "MIN","a1", "MIN", "ROLE", "DCU_MIN_DISPATCH", true);

        when(teamRepository.findByUuid(team1UUID)).thenReturn(team);
        when(caseTypeRepository.findByType(any())).thenReturn(caseType);

        Set<PermissionDto> permissions = new HashSet<PermissionDto>() {{
            add(new PermissionDto("MIN", AccessLevel.READ));
            add(new PermissionDto("MIN", AccessLevel.OWNER));
        }};

        Set<String> permissionPaths = new HashSet<String>() {{
            add("/UNIT/" + team1UUID + "/MIN/READ");
            add("/UNIT/" + team1UUID + "/MIN/OWNER");
        }};

        assertThat(team.getPermissions().size()).isEqualTo(0);
        teamService.updateTeamPermissions(team1UUID, permissions);
        assertThat(team.getPermissions().size()).isEqualTo(2);


        verify(teamRepository, times(1)).findByUuid(team1UUID);
        verify(keycloakService, times(1)).updateUserTeamGroups("/UNIT/" + team1UUID.toString(),permissionPaths);
        verifyNoMoreInteractions(teamRepository);
    }



    private List<Team> getTeams() {
        CaseTypeEntity caseType = new CaseTypeEntity(1L, "MIN","a1", "MIN", "ROLE","DCU_MIN_DISPATCH", true);
        Set<Permission> permissions = new HashSet<Permission>() {{
            add(new Permission(1L, AccessLevel.OWNER, null, caseType));
            add(new Permission(1L, AccessLevel.OWNER, null, caseType));
        }};

        return new ArrayList<Team>() {{
            add(new Team("Team1", team1UUID,permissions));
            add(new Team( "Team2", team2UUID, new HashSet<>()));
        }};
    }


}