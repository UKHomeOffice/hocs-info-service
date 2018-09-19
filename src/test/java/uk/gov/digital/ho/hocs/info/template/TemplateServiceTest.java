package uk.gov.digital.ho.hocs.info.template;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.digital.ho.hocs.info.RequestData;
import uk.gov.digital.ho.hocs.info.casetype.CaseTypeService;
import uk.gov.digital.ho.hocs.info.exception.EntityPermissionException;
import uk.gov.digital.ho.hocs.info.repositories.TemplateRepository;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TemplateServiceTest {

    @Mock
    private TemplateRepository templateRepository;

    @Mock
    private RequestData requestData;

    @Mock
    private CaseTypeService caseTypeService;

    private TemplateService templateService;

    @Before
    public void setUp() {
        this.templateService = new TemplateService(templateRepository, caseTypeService, requestData);
    }

    @Test
    public void shouldReturnListOfTemplates() throws EntityPermissionException {
        when(caseTypeService.hasPermissionForCaseType(any())).thenReturn(true);
        templateService.getTemplates("MIN");
        verify(templateRepository, times(1)).findActiveTemplateByCaseType(any());
        verifyNoMoreInteractions(templateRepository);
    }

    @Test
    public void shouldReturnTemplateKey() throws EntityPermissionException {
        templateService.getTemplateKey(UUID.randomUUID());
        verify(templateRepository, times(1)).findTemplateByUuid(any());
        verifyNoMoreInteractions(templateRepository);
    }

    @Test(expected = EntityPermissionException.class)
    public void shouldThrowExemptionWhenCaseTypeNotValidForPermissionCheck() throws EntityPermissionException {
        templateService.getTemplates(null);
    }
}
