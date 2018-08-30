package uk.gov.digital.ho.hocs.info.template;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.digital.ho.hocs.info.RequestData;
import uk.gov.digital.ho.hocs.info.casetype.CaseTypeService;
import uk.gov.digital.ho.hocs.info.entities.Template;
import uk.gov.digital.ho.hocs.info.exception.EntityPermissionException;
import uk.gov.digital.ho.hocs.info.repositories.TemplateRepository;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
    public void shouldReturnTemplate() throws EntityPermissionException {
        when(caseTypeService.hasPermissionForCaseType(any())).thenReturn(true);
        Template template = templateService.getTemplate("MIN");
        verify(templateRepository, times(1)).findActiveTemplateByCaseType(any());
    }

    @Test(expected = EntityPermissionException.class)
    public void shouldThrowExemptionWhenCaseTypeNotValidForPermissionCheck() throws EntityPermissionException {
        templateService.getTemplate(null);
    }
}
