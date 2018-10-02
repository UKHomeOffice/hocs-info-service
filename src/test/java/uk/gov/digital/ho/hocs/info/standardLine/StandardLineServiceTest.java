package uk.gov.digital.ho.hocs.info.standardLine;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.digital.ho.hocs.info.RequestData;
import uk.gov.digital.ho.hocs.info.casetype.CaseTypeService;
import uk.gov.digital.ho.hocs.info.entities.StandardLine;
import uk.gov.digital.ho.hocs.info.exception.EntityPermissionException;
import uk.gov.digital.ho.hocs.info.repositories.StandardLineRepository;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class StandardLineServiceTest {

    @Mock
    private StandardLineRepository standardLineRepository;

    private StandardLineService standardLineService;

    UUID uuid = UUID.randomUUID();

    @Before
    public void setUp() {
        this.standardLineService = new StandardLineService(standardLineRepository);
    }

    @Test
    public void shouldReturnListOfStandardLineForPrimaryTopic() throws EntityPermissionException {
        List<StandardLine> standardLines = standardLineService.getStandardLines(uuid);
        verify(standardLineRepository, times(1)).findStandardLinesByTopic(any());
        verifyNoMoreInteractions(standardLineRepository);
    }
}
