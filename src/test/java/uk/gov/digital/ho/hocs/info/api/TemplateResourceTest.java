package uk.gov.digital.ho.hocs.info.api;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uk.gov.digital.ho.hocs.info.api.dto.CreateTemplateDocumentDto;
import uk.gov.digital.ho.hocs.info.api.dto.TemplateDto;
import uk.gov.digital.ho.hocs.info.domain.model.Template;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TemplateResourceTest {

    private static final String DISPLAY_NAME = "display";
    private static final String CASE_TYPE = "MIN";

    @Mock
    private TemplateService templateService;

    private TemplateResource templateResource;

    @Before
    public void setUp() {
        templateResource = new TemplateResource(templateService);
    }

    @Test
    public void shouldReturnTemplates() {

        when(templateService.getActiveTemplates()).thenReturn(List.of(new Template(DISPLAY_NAME, CASE_TYPE)));

        ResponseEntity<List<TemplateDto>> response =
                templateResource.getTemplates();

        verify(templateService).getActiveTemplates();
        verifyNoMoreInteractions(templateService);
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void shouldReturnTemplate() {
        UUID templateUUID = UUID.randomUUID();
        when(templateService.getTemplate(templateUUID)).thenReturn(new Template(DISPLAY_NAME, CASE_TYPE));

        ResponseEntity<TemplateDto> response =
                templateResource.getTemplate(templateUUID);

        verify(templateService).getTemplate(templateUUID);
        verifyNoMoreInteractions(templateService);
        assertThat(response).isNotNull();
        assertThat(response.getBody().getDisplayName()).isEqualTo(DISPLAY_NAME);
        assertThat(response.getBody().getCaseType()).isEqualTo(CASE_TYPE);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void shouldReturnTemplatesForRequestedCaseType() {

        when(templateService.getTemplatesForCaseType(CASE_TYPE)).thenReturn(List.of(new Template(DISPLAY_NAME, CASE_TYPE)));

        ResponseEntity<List<TemplateDto>> response =
                templateResource.getTemplatesForCaseType(CASE_TYPE);

        verify(templateService).getTemplatesForCaseType(CASE_TYPE);
        verifyNoMoreInteractions(templateService);
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void shouldCreateTemplateForCaseType() {
        CreateTemplateDocumentDto createTemplateDocumentDto = new CreateTemplateDocumentDto();

        ResponseEntity response =
                templateResource.createTemplate(createTemplateDocumentDto);

        verify(templateService).createTemplate(createTemplateDocumentDto);
        verifyNoMoreInteractions(templateService);
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void shouldDeleteTemplate() {
        UUID uuid = UUID.randomUUID();
        ResponseEntity response =
                templateResource.deleteTemplate(uuid);

        verify(templateService).deleteTemplate(uuid);
        verifyNoMoreInteractions(templateService);
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}