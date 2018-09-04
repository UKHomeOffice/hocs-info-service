package uk.gov.digital.ho.hocs.info.template;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uk.gov.digital.ho.hocs.info.dto.GetTemplateResponse;
import uk.gov.digital.ho.hocs.info.entities.CaseTypeEntity;
import uk.gov.digital.ho.hocs.info.entities.Template;
import uk.gov.digital.ho.hocs.info.exception.EntityPermissionException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TemplateResourceTest {

    public static final String MIN = "MIN";
    @Mock
    private TemplateService templateService;

    private TemplateResource templateResource;

    @Before
    public void setUp() {
        templateResource = new TemplateResource(templateService);
    }

    @Test
    public void shouldReturnTemplateForRequestedCaseType() throws EntityPermissionException {

        when(templateService.getTemplate(MIN)).thenReturn(new Template());

        ResponseEntity<GetTemplateResponse> response =
                templateResource.getTemplateForCaseTypes(MIN);

        verify(templateService, times(1)).getTemplate(MIN);
        verifyNoMoreInteractions(templateService);
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}