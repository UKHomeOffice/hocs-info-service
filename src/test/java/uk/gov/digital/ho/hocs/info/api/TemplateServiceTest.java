package uk.gov.digital.ho.hocs.info.api;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.digital.ho.hocs.info.api.dto.CreateTemplateDocumentDto;
import uk.gov.digital.ho.hocs.info.client.caseworkclient.CaseworkClient;
import uk.gov.digital.ho.hocs.info.client.documentclient.DocumentClient;
import uk.gov.digital.ho.hocs.info.client.documentclient.model.ManagedDocumentType;
import uk.gov.digital.ho.hocs.info.domain.exception.ApplicationExceptions;
import uk.gov.digital.ho.hocs.info.domain.model.Template;
import uk.gov.digital.ho.hocs.info.domain.repository.TemplateRepository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TemplateServiceTest {

    @Mock
    private TemplateRepository templateRepository;

    @Mock
    private DocumentClient documentClient;

    @Mock
    private CaseworkClient caseworkClient;

    private TemplateService templateService;

    private static final String DISPLAY_NAME = "dn";

    private static final String CASE_TYPE = "MIN";

    private static final UUID NEW_DOCUMENT_UUID = UUID.randomUUID();

    @Before
    public void setUp() {
        this.templateService = new TemplateService(templateRepository, documentClient, caseworkClient);
    }

    @Test
    public void shouldReturnTemplatesForCaseType() throws ApplicationExceptions.EntityPermissionException {

        when(templateRepository.findActiveTemplatesByCaseType(CASE_TYPE)).thenReturn(List.of(new Template()));
        List<Template> results = templateService.getTemplatesForCaseType(CASE_TYPE);
        verify(templateRepository).findActiveTemplatesByCaseType(CASE_TYPE);

        assertThat(results).size().isEqualTo(1);
        verifyNoMoreInteractions(templateRepository, documentClient, caseworkClient);
    }

    @Test
    public void shouldReturnTemplate() throws ApplicationExceptions.EntityPermissionException {

        when(templateRepository.findActiveTemplates()).thenReturn(List.of(new Template()));
        templateService.getActiveTemplates();
        verify(templateRepository).findActiveTemplates();

        verifyNoMoreInteractions(templateRepository, documentClient, caseworkClient);
    }

    @Test
    public void shouldCreateNewTemplate() {
        CreateTemplateDocumentDto request = new CreateTemplateDocumentDto(DISPLAY_NAME, CASE_TYPE, "URL");

        when(documentClient.createDocument(any(UUID.class), eq(request.getDisplayName()), eq("URL"),
            eq(ManagedDocumentType.TEMPLATE))).thenReturn(NEW_DOCUMENT_UUID);

        templateService.createTemplate(request);

        verify(documentClient).createDocument(any(UUID.class), eq(DISPLAY_NAME), eq("URL"),
            eq(ManagedDocumentType.TEMPLATE));
        verify(templateRepository).save(any());
        verify(caseworkClient).clearCachedTemplateForCaseType(CASE_TYPE);
        verifyNoMoreInteractions(templateRepository, documentClient, caseworkClient);

    }

    @Test
    public void shouldDeleteTemplate() {

        UUID templateUUID = UUID.randomUUID();
        UUID documentUUID = UUID.randomUUID();
        Template testTemplate = new Template(1L, templateUUID, documentUUID, DISPLAY_NAME, CASE_TYPE, Boolean.FALSE);
        when(templateRepository.findActiveTemplateByUuid(templateUUID)).thenReturn(testTemplate);

        templateService.deleteTemplate(testTemplate.getUuid());

        verify(templateRepository).findActiveTemplateByUuid(templateUUID);
        verify(templateRepository).save(any());
        verify(documentClient).deleteDocument(documentUUID);
        verify(caseworkClient).clearCachedTemplateForCaseType(CASE_TYPE);

        verifyNoMoreInteractions(templateRepository, documentClient, caseworkClient);

    }

    @Test
    public void shouldGetEmptyTemplateList() {
        when(templateRepository.findActiveTemplatesByCaseType(CASE_TYPE)).thenReturn(List.of());

        List<Template> result = templateService.getTemplatesForCaseType(CASE_TYPE);

        assertThat(result).isNullOrEmpty();

        verify(templateRepository).findActiveTemplatesByCaseType(CASE_TYPE);
        verifyNoMoreInteractions(templateRepository, documentClient, caseworkClient);
    }

    @Test
    public void shouldGetTemplateWithValidParams() {
        Template testTemplate = new Template(DISPLAY_NAME, CASE_TYPE);
        when(templateRepository.findActiveTemplateByUuid(testTemplate.getUuid())).thenReturn(testTemplate);

        Template template = templateService.getTemplate(testTemplate.getUuid());

        assertThat(template).isNotNull();
        assertThat(template.getDisplayName()).isEqualTo(DISPLAY_NAME);
        assertThat(template.getCaseType()).isEqualTo(CASE_TYPE);

        verify(templateRepository).findActiveTemplateByUuid(testTemplate.getUuid());
        verifyNoMoreInteractions(templateRepository, documentClient, caseworkClient);
    }

    @Test(expected = ApplicationExceptions.EntityNotFoundException.class)
    public void shouldNotGetTemplateWithValidParams() {
        when(templateRepository.findActiveTemplateByUuid(any())).thenReturn(null);

        templateService.getTemplate(UUID.randomUUID());

        verifyNoMoreInteractions(templateRepository, documentClient, caseworkClient);
    }

}
