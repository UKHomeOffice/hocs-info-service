package uk.gov.digital.ho.hocs.info.standardLines;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.digital.ho.hocs.info.RequestData;
import uk.gov.digital.ho.hocs.info.casetype.CaseTypeService;
import uk.gov.digital.ho.hocs.info.entities.StandardLines;
import uk.gov.digital.ho.hocs.info.entities.Template;
import uk.gov.digital.ho.hocs.info.exception.EntityPermissionException;
import uk.gov.digital.ho.hocs.info.repositories.StandardLinesRepository;
import uk.gov.digital.ho.hocs.info.repositories.TemplateRepository;
import uk.gov.digital.ho.hocs.info.template.TemplateService;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class StandardLinesServiceTest {

    @Mock
    private StandardLinesRepository standardLinesRepository;

    @Mock
    private RequestData requestData;

    @Mock
    private CaseTypeService caseTypeService;

    private StandardLinesService standardLinesService;

    UUID uuid = UUID.randomUUID();

    @Before
    public void setUp() {
        this.standardLinesService = new StandardLinesService(standardLinesRepository, caseTypeService, requestData);
    }

    @Test
    public void shouldReturnTemplate() throws EntityPermissionException {
        when(caseTypeService.hasPermissionForCaseType(any())).thenReturn(true);
        StandardLines standardLines = standardLinesService.getStandardLines("MIN",uuid);
        verify(standardLinesRepository, times(1)).findStandardLinesByCaseTopic(any());
    }

    @Test(expected = EntityPermissionException.class)
    public void shouldThrowExemptionWhenCaseTypeNotValidForPermissionCheck() throws EntityPermissionException {
        standardLinesService.getStandardLines(null,uuid);
    }
}
