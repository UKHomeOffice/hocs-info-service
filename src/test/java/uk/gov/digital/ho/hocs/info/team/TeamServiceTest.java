package uk.gov.digital.ho.hocs.info.team;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.digital.ho.hocs.info.dto.TeamDto;
import uk.gov.digital.ho.hocs.info.entities.CaseTypeEntity;
import uk.gov.digital.ho.hocs.info.entities.Permission;
import uk.gov.digital.ho.hocs.info.entities.Team;
import uk.gov.digital.ho.hocs.info.entities.Unit;
import uk.gov.digital.ho.hocs.info.repositories.CaseTypeRepository;
import uk.gov.digital.ho.hocs.info.repositories.TeamRepository;
import uk.gov.digital.ho.hocs.info.repositories.UnitRepository;
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
    public void shouldGetAllTeamsForUnit() {

        UUID unitUUID = UUID.randomUUID();
        when(teamRepository.findTeamsByUnitUUID(unitUUID)).thenReturn(getTeams().stream().collect(Collectors.toSet()));

        teamService.getTeamsForUnit(unitUUID);

        verify(teamRepository, times(1)).findTeamsByUnitUUID(unitUUID);
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

    @Test
    public void shouldAddTeamToRepositoryAndKeycloak() {

        UUID unitUUID = UUID.randomUUID();
        Team team = new Team( "Team1", team1UUID);
        Unit unit = new Unit(1L,"UNIT1", "UNIT1", unitUUID,true,
                new HashSet<Team>(){{
                add(team);
                }});

        TeamDto teamDto = new TeamDto( "Team1", team1UUID, new HashSet<>());

        when(teamRepository.findByUuid(team1UUID)).thenReturn(null);
        when(unitRepository.findByUuid(unitUUID)).thenReturn(unit);

        teamService.createTeam(teamDto, unitUUID);

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
        Team team = new Team( "Team1", team1UUID);
        Unit unit = new Unit(1L,"UNIT1", "UNIT1", unitUUID,true,
                new HashSet<Team>(){{
                    add(team);
                }});

        TeamDto teamDto = new TeamDto( "Team1", team1UUID, new HashSet<>());
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
    public void shouldGetTeamPermissions() {
        when(teamRepository.findByUuid(team1UUID)).thenReturn(getTeams().get(0));

        TeamDto result = teamService.getTeam(team1UUID);

        assertThat(result.getPermissions().size()).isEqualTo(1);
        verify(teamRepository, times(1)).findByUuid(team1UUID);
        verifyNoMoreInteractions(teamRepository);
    }


    @Test
    public void shouldAddUserToTeam() {

        UUID unitUUID = UUID.randomUUID();
        UUID userUUID = UUID.randomUUID();

        Set<Permission> permissions = new HashSet<>();
        Unit unit = new Unit(1L, "a unit", "UNIT", unitUUID, true, new HashSet<>());
        CaseTypeEntity caseType = new CaseTypeEntity(1L, "MIN", "MIN", "ROLE");
        Permission permission = new Permission(1L, team1UUID, "MIN", AccessLevel.OWNER, null, caseType);
        permissions.add(permission);
        Team team = new Team(1L, "a team", team1UUID, unitUUID, unit, permissions);

        String unitGroupPath = "UNIT";
        String teamGroupPath = "/UNIT/" + team1UUID.toString();
        String caseGroupPath = "/UNIT/" + team1UUID.toString() + "/MIN";
        String accessLevelGroupPath = "/UNIT/" + team1UUID.toString() + "/MIN/OWNER";

        when(teamRepository.findByUuid(team1UUID)).thenReturn(team);
        doNothing().when(keycloakService).createUnitGroupIfNotExists(unitGroupPath);
        doNothing().when(keycloakService).createGroupPathIfNotExists(unitGroupPath, team1UUID.toString());
        doNothing().when(keycloakService).createGroupPathIfNotExists(teamGroupPath, "MIN");
        doNothing().when(keycloakService).createGroupPathIfNotExists(caseGroupPath, "OWNER");
        doNothing().when(keycloakService).addUserToGroup(userUUID, accessLevelGroupPath);

        teamService = new TeamService(teamRepository, unitRepository, caseTypeRepository, keycloakService);
        teamService.addUserToTeam(userUUID, team1UUID);

        verify(keycloakService, times(1)).createGroupPathIfNotExists(unitGroupPath, team1UUID.toString());
        verify(keycloakService, times(1)).createGroupPathIfNotExists(teamGroupPath, "MIN");
        verify(keycloakService, times(1)).createGroupPathIfNotExists(caseGroupPath, "OWNER");
        verify(keycloakService, times(1)).addUserToGroup(userUUID, accessLevelGroupPath);
    }

    private List<Team> getTeams() {


        CaseTypeEntity caseType = new CaseTypeEntity(1L, "MIN", "MIN", "ROLE");

        Set<Permission> permissions = new HashSet<Permission>() {{
            add(new Permission(1L, team1UUID, "MIN", AccessLevel.OWNER, null, caseType));
            add(new Permission(1L, team1UUID, "MIN", AccessLevel.OWNER, null, caseType));
        }};

        return new ArrayList<Team>() {{
            add(new Team("Team1", team1UUID,permissions));
            add(new Team( "Team2", team2UUID, new HashSet<>()));
        }};
    }


}