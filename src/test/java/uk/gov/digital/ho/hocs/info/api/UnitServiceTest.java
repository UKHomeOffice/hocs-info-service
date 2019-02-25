package uk.gov.digital.ho.hocs.info.api;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.digital.ho.hocs.info.api.dto.UnitDto;
import uk.gov.digital.ho.hocs.info.domain.exception.ApplicationExceptions;
import uk.gov.digital.ho.hocs.info.domain.model.Team;
import uk.gov.digital.ho.hocs.info.domain.model.Unit;
import uk.gov.digital.ho.hocs.info.domain.repository.TeamRepository;
import uk.gov.digital.ho.hocs.info.domain.repository.UnitRepository;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.extractProperty;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UnitServiceTest {

    @Mock
    UnitRepository unitRepository;

    @Mock
    TeamService teamService;

    UnitService unitService;

    @Before
    public void setUp() {
        unitService = new UnitService(unitRepository, teamService);
    }



    @Test
    public void shouldCreateUnit() {
        UnitDto unit = new UnitDto("UNIT1", "TEST1");
        unitService.createUnit(unit);

        verify(unitRepository, times(1)).save(any(Unit.class));

    }

    @Test
    public void shouldDeleteUnitWithNoTeams() {

        UUID unitUUID = UUID.randomUUID();
        Unit unit = new Unit("a unit", "U", true);

        Set<Team> teams = new HashSet<Team>();

        when(unitRepository.findByUuid(unitUUID)).thenReturn(unit);
        when(teamService.findActiveTeamsByUnitUuid(unitUUID)).thenReturn(teams);

        unitService.deleteUnit(unitUUID);

        verify(unitRepository, times(1)).findByUuid(unitUUID);
        verify(unitRepository, times(1)).save(unit);
        verifyNoMoreInteractions(unitRepository);
    }

    @Test (expected = ApplicationExceptions.UnitDeleteException.class)
    public void shouldNotDeleteUnitWithActiveTeams() {

        UUID unitUUID = UUID.randomUUID();
        Unit unit = new Unit("a unit", "U", true);

        Set<Team> teams = new HashSet<Team>();
        teams.add(new Team("a team", true));

        when(teamService.findActiveTeamsByUnitUuid(unitUUID)).thenReturn(teams);

        unitService.deleteUnit(unitUUID);

        verify(unitRepository, times(1)).findByUuid(unitUUID);
        verify(unitRepository, times(1)).save(unit);
        verifyNoMoreInteractions(unitRepository);

    }

    @Test
    public void shouldGetAllUnits() {

        Set<Unit> units = new HashSet<Unit>(){{
            add(new Unit( "UNIT1", "TEST1",true));
            add(new Unit( "UNIT2", "TEST2",true));

        }};

        when(unitRepository.findAll()).thenReturn(units);

        Set<UnitDto> result = unitService.getAllUnits();

        assertThat(result.size()).isEqualTo(2);
        assertThat(extractProperty("displayName").ofType(String.class).from(result)).contains("UNIT1", "UNIT2");
        verify(unitRepository, times(1)).findAll();
    }


    @Test
    public void shouldNotReturnTeams() {

        Set<Unit> units = new HashSet<Unit>(){{
            add(new Unit("UNIT1", "TEST1",true));
        }};
        when(unitRepository.findAll()).thenReturn(units);

        Set<UnitDto> result = unitService.getAllUnits();

        assertThat(extractProperty("teams").ofType(String.class).from(result)).containsOnlyNulls();
        verify(unitRepository, times(1)).findAll();
    }


    @Test
    public void shouldGetUnit() {

        UUID unitUUID = UUID.randomUUID();

        when(unitRepository.findByUuid(unitUUID)).thenReturn(new Unit("UNIT1", "TEST1",true));
        unitService.getUnit(unitUUID);

        verify(unitRepository, times(1)).findByUuid(unitUUID);
        verifyNoMoreInteractions(unitRepository);
    }

    @Test(expected = ApplicationExceptions.EntityNotFoundException.class)
    public void shouldNotGetUnit(){

        UUID unitUUID = UUID.randomUUID();

        when(unitRepository.findByUuid(unitUUID)).thenReturn(null);

        unitService.getUnit(unitUUID);
        verifyNoMoreInteractions(unitRepository);
    }
}