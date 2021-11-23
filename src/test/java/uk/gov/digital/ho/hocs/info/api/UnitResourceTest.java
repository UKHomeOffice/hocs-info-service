package uk.gov.digital.ho.hocs.info.api;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uk.gov.digital.ho.hocs.info.api.dto.UnitDto;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UnitResourceTest {

    @Mock
    UnitService unitService;

    UnitResource unitResource;

    @Before
    public void setUp() {
        unitResource = new UnitResource(unitService);
    }



    @Test
    public void getAllUnitsShouldCallCollaborators() {
        Set<UnitDto> units = new HashSet<UnitDto>(){{
            add(new UnitDto("Unit1", UUID.randomUUID().toString(), "UNIT1"));
            add(new UnitDto("Unit2", UUID.randomUUID().toString(), "UNIT2"));
        }};

        when(unitService.getAllUnits()).thenReturn(units);

        ResponseEntity<Set<UnitDto>> result = unitResource.getAllUnits();
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody().size()).isEqualTo(2);
        verify(unitService, Mockito.times(1)).getAllUnits();
    }

    @Test
    public void createUnitShouldCallCollaborators() {
        UnitDto unit = new UnitDto("Unit1", UUID.randomUUID().toString(), "UNIT1");

        ResponseEntity result = unitResource.createUnit(unit);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(unitService, Mockito.times(1)).createUnit(unit);
    }

    @Test
    public void shouldDeleteUnit(){

        UUID unitUUID = UUID.randomUUID();

        ResponseEntity response = unitResource.deleteUnit(unitUUID);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(unitService, times(1)).deleteUnit(unitUUID);
        verifyNoMoreInteractions(unitService);



    }
}