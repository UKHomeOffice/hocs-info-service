package uk.gov.digital.ho.hocs.info.api.unit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.digital.ho.hocs.info.api.dto.UnitDto;
import uk.gov.digital.ho.hocs.info.domain.model.Unit;
import uk.gov.digital.ho.hocs.info.domain.repository.UnitRepository;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.extractProperty;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UnitServiceTest {

    @Mock
    UnitRepository unitRepository;


    UnitService unitService;


    @Test
    public void shouldCreateUnit() {
        UnitDto unit = new UnitDto("UNIT1", "TEST1");
        unitService = new UnitService(unitRepository);
        unitService.createUnit(unit);

        verify(unitRepository, times(1)).save(any(Unit.class));

    }

    @Test
    public void shouldGetAllUnits() {

        UUID unitUUID = UUID.randomUUID();

        Set<Unit> units = new HashSet<Unit>(){{
            add(new Unit(1L, "UNIT1", "TEST1", unitUUID,true,new HashSet<>()));
            add(new Unit(2L, "UNIT2", "TEST2", unitUUID,true,new HashSet<>()));

        }};

        when(unitRepository.findAll()).thenReturn(units);

        unitService = new UnitService(unitRepository);
        Set<UnitDto> result = unitService.getAllUnits();

        assertThat(result.size()).isEqualTo(2);
        assertThat(extractProperty("displayName").ofType(String.class).from(result)).contains("UNIT1", "UNIT2");
        verify(unitRepository, times(1)).findAll();
    }


    @Test
    public void shouldNotReturnTeams() {

        UUID unitUUID = UUID.randomUUID();

        Set<Unit> units = new HashSet<Unit>(){{
            add(new Unit(1L, "UNIT1", "TEST1", unitUUID,true,new HashSet<>()));
        }};
        when(unitRepository.findAll()).thenReturn(units);

        unitService = new UnitService(unitRepository);
        Set<UnitDto> result = unitService.getAllUnits();

        assertThat(extractProperty("teams").ofType(String.class).from(result)).containsOnlyNulls();
        verify(unitRepository, times(1)).findAll();
    }




}