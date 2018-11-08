package uk.gov.digital.ho.hocs.info.template;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uk.gov.digital.ho.hocs.info.dto.CreateTemplateDocumentDto;
import uk.gov.digital.ho.hocs.info.dto.GetTemplateResponse;
import uk.gov.digital.ho.hocs.info.entities.Template;
import uk.gov.digital.ho.hocs.info.exception.EntityPermissionException;

import java.util.ArrayList;
import java.util.UUID;

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

        when(templateService.getTemplates(MIN)).thenReturn(new Template("display","MIN" , UUID.randomUUID()));

        ResponseEntity<GetTemplateResponse> response =
                templateResource.getTemplatesForCaseType(MIN);

        verify(templateService, times(1)).getTemplates(MIN);
        verifyNoMoreInteractions(templateService);
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void shouldReturnTemplateNotFoundForRequestedCaseTypeWhenNotTemplate() throws EntityPermissionException {

        when(templateService.getTemplates(MIN)).thenReturn(null);

        ResponseEntity<GetTemplateResponse> response =
                templateResource.getTemplatesForCaseType(MIN);

        verify(templateService, times(1)).getTemplates(MIN);
        verifyNoMoreInteractions(templateService);
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void shouldCreateTemplateForCaseType() {
        CreateTemplateDocumentDto createTemplateDocumentDto = new CreateTemplateDocumentDto();

        ResponseEntity response =
                templateResource.createDocument(createTemplateDocumentDto);

        verify(templateService, times(1)).createTemplateDocument(createTemplateDocumentDto);
        verifyNoMoreInteractions(templateService);
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}