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
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TemplateServiceTest {

    @Mock
    private TemplateRepository templateRepository;

    private TemplateService templateService;

    @Before
    public void setUp() {
        this.templateService = new TemplateService(templateRepository);
    }

    @Test
    public void shouldReturnTemplate() throws EntityPermissionException {
        templateService.getTemplate("MIN");
        verify(templateRepository, times(1)).findActiveTemplateByCaseType(any());
        verifyNoMoreInteractions(templateRepository);
    }
}
