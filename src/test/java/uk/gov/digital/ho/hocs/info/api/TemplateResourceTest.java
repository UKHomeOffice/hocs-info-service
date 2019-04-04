package uk.gov.digital.ho.hocs.info.api;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uk.gov.digital.ho.hocs.info.api.dto.CreateTemplateDocumentDto;
import uk.gov.digital.ho.hocs.info.api.dto.GetTemplateResponse;
import uk.gov.digital.ho.hocs.info.domain.model.Template;

import java.util.Set;

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
    public void shouldReturnTemplates() {

        when(templateService.getActiveTemplates()).thenReturn(Set.of(new Template("display","MIN" )));

        ResponseEntity<Set<GetTemplateResponse>> response =
                templateResource.getTemplates();

        verify(templateService, times(1)).getActiveTemplates();
        verifyNoMoreInteractions(templateService);
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void shouldReturnTemplateForRequestedCaseType() {

        when(templateService.getTemplateForCaseType(MIN)).thenReturn(new Template("display","MIN" ));

        ResponseEntity<GetTemplateResponse> response =
                templateResource.getTemplatesForCaseType(MIN);

        verify(templateService, times(1)).getTemplateForCaseType(MIN);
        verifyNoMoreInteractions(templateService);
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void shouldCreateTemplateForCaseType() {
        CreateTemplateDocumentDto createTemplateDocumentDto = new CreateTemplateDocumentDto();

        ResponseEntity response =
                templateResource.createTemplate(createTemplateDocumentDto);

        verify(templateService, times(1)).createTemplate(createTemplateDocumentDto);
        verifyNoMoreInteractions(templateService);
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}