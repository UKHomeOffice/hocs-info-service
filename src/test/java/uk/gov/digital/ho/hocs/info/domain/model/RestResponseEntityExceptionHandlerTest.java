package uk.gov.digital.ho.hocs.info.domain.model;

import org.junit.Test;
import org.mockito.Mock;
import uk.gov.digital.ho.hocs.info.api.UnitService;
import uk.gov.digital.ho.hocs.info.domain.exception.ApplicationExceptions;
import uk.gov.digital.ho.hocs.info.domain.repository.UnitRepository;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;


public class RestResponseEntityExceptionHandlerTest {
    @Mock
    UnitRepository unitRepository;
    @Mock
    UnitService unitService;

    @Test(expected = ApplicationExceptions.EntityAlreadyExistsException.class)
    public void shouldReturnConflictHTTPStatus() {
        Unit newUnit = new Unit(unit.getDisplayName(),unit.getShortCode(), true);

        when(unitService.createUnit(newUnit)).thenThrow(ApplicationExceptions.EntityAlreadyExistsException.class);
    }

    @Test(expected = ApplicationExceptions.EntityNotFoundException.class)
    public void shouldNotGetUnit(){

        //UUID unitUUID = UUID.randomUUID();

        //when(unitRepository.findByUuid(unitUUID)).thenReturn(null);

        unitService.getUnit(unitUUID);
        verifyNoMoreInteractions(unitRepository);
    }
}
