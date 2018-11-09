package uk.gov.digital.ho.hocs.info.template;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.digital.ho.hocs.info.RequestData;
import uk.gov.digital.ho.hocs.info.casetype.CaseTypeService;
import uk.gov.digital.ho.hocs.info.documentClient.DocumentClient;
import uk.gov.digital.ho.hocs.info.documentClient.model.ManagedDocumentType;
import uk.gov.digital.ho.hocs.info.dto.CreateTemplateDocumentDto;
import uk.gov.digital.ho.hocs.info.entities.Template;
import uk.gov.digital.ho.hocs.info.exception.EntityCreationException;
import uk.gov.digital.ho.hocs.info.exception.EntityNotFoundException;
import uk.gov.digital.ho.hocs.info.exception.EntityPermissionException;
import uk.gov.digital.ho.hocs.info.repositories.TemplateRepository;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TemplateServiceTest {

    @Mock
    private TemplateRepository templateRepository;
    
    @Mock
    private DocumentClient documentClient;

    private TemplateService templateService;
    private static final UUID TEMPLATE_EXT_REF = UUID.fromString("88888888-8888-8888-8888-888888888888");
    private static final UUID DOCUMENT_UUID = UUID.randomUUID();
    private static final UUID NEW_DOCUMENT_UUID = UUID.randomUUID();
    private static final String DISPLAY_NAME = "dn";
    private static final String CASE_TYPE = "MIN";

    @Before
    public void setUp() {
        this.templateService = new TemplateService(templateRepository,documentClient);
    }

    @Test
    public void shouldReturnListOfTemplates() throws EntityPermissionException {
        when(templateRepository.findActiveTemplateByCaseType(CASE_TYPE)).thenReturn(new Template());
        templateService.getTemplates(CASE_TYPE);
        verify(templateRepository, times(1)).findActiveTemplateByCaseType(CASE_TYPE);
        verifyNoMoreInteractions(templateRepository);
    }

    @Test
    public void shouldCreateNewTemplate(){
        CreateTemplateDocumentDto request = new CreateTemplateDocumentDto(DISPLAY_NAME, CASE_TYPE,"URL");

        when(documentClient.createDocument(TEMPLATE_EXT_REF, request.getDisplayName(), ManagedDocumentType.TEMPLATE)).thenReturn(NEW_DOCUMENT_UUID);
        when(templateRepository.findActiveTemplateByCaseType( request.getCaseType())).thenReturn(null);

        templateService.createTemplateDocument(request);

        verify(documentClient,times(1)).createDocument(TEMPLATE_EXT_REF, DISPLAY_NAME,ManagedDocumentType.TEMPLATE );
        verify(templateRepository, times(1)).findActiveTemplateByCaseType(request.getCaseType());
        verify(templateRepository, times(1)).save(any());
        verify(documentClient).processDocument(ManagedDocumentType.TEMPLATE, NEW_DOCUMENT_UUID, "URL");
        verifyNoMoreInteractions(templateRepository);
        verifyNoMoreInteractions(documentClient);
    }

    @Test
    public void shouldCreateStandardLineExpiringPrevious(){
        CreateTemplateDocumentDto request = new CreateTemplateDocumentDto(DISPLAY_NAME, CASE_TYPE,"URL");

        when(documentClient.createDocument(TEMPLATE_EXT_REF, request.getDisplayName(), ManagedDocumentType.TEMPLATE)).thenReturn(NEW_DOCUMENT_UUID);
        when(templateRepository.findActiveTemplateByCaseType( request.getCaseType())).thenReturn(new Template(DISPLAY_NAME, CASE_TYPE, DOCUMENT_UUID));

        templateService.createTemplateDocument(request);

        verify(documentClient,times(1)).createDocument(TEMPLATE_EXT_REF, DISPLAY_NAME,ManagedDocumentType.TEMPLATE );
        verify(templateRepository, times(1)).findActiveTemplateByCaseType(request.getCaseType());
        verify(templateRepository, times(2)).save(any());
        verify(documentClient).deleteDocument(TEMPLATE_EXT_REF, DOCUMENT_UUID);
        verify(documentClient).processDocument(ManagedDocumentType.TEMPLATE, NEW_DOCUMENT_UUID, "URL");
        verifyNoMoreInteractions(templateRepository);
        verifyNoMoreInteractions(documentClient);
    }

    @Test(expected = EntityCreationException.class)
    public void shouldThrowExemptionWhenCreateTemplateDocumentDTOIsNull() {
        templateService.createTemplateDocument(null);
    }

    @Test(expected = EntityNotFoundException.class)
    public void shouldNotGetCaseWithValidParamsNotFoundException() {
        String caseType = CASE_TYPE;

        when(templateRepository.findActiveTemplateByCaseType(caseType)).thenReturn(null);

        templateService.getTemplates(caseType);
    }

}
