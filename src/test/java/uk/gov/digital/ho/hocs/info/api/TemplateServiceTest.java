package uk.gov.digital.ho.hocs.info.api;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.digital.ho.hocs.info.client.documentClient.DocumentClient;
import uk.gov.digital.ho.hocs.info.client.documentClient.model.ManagedDocumentType;
import uk.gov.digital.ho.hocs.info.api.dto.CreateTemplateDocumentDto;
import uk.gov.digital.ho.hocs.info.domain.exception.ApplicationExceptions;
import uk.gov.digital.ho.hocs.info.domain.model.Template;
import uk.gov.digital.ho.hocs.info.domain.repository.TemplateRepository;

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
    private CaseTypeService caseTypeService;

    private TemplateService templateService;
    private static final UUID TEMPLATE_EXT_REF = UUID.fromString("88888888-8888-8888-8888-888888888888");
    private static final UUID DOCUMENT_UUID = UUID.randomUUID();
    private static final UUID NEW_DOCUMENT_UUID = UUID.randomUUID();
    private static final String DISPLAY_NAME = "dn";
    private static final String CASE_TYPE = "MIN";

    @Before
    public void setUp() {
        this.templateService = new TemplateService(templateRepository,documentClient, caseTypeService);
    }

    @Test
    public void shouldReturnListOfTemplates() throws ApplicationExceptions.EntityPermissionException {

        when(templateRepository.findActiveTemplateByCaseType(CASE_TYPE)).thenReturn(new Template());
        templateService.getTemplateForCaseType(CASE_TYPE);
        verify(templateRepository, times(1)).findActiveTemplateByCaseType(CASE_TYPE);

        verifyNoMoreInteractions(templateRepository);
    }

    @Test
    public void shouldCreateNewTemplate(){
        CreateTemplateDocumentDto request = new CreateTemplateDocumentDto(DISPLAY_NAME, CASE_TYPE,"URL");

        when(documentClient.createDocument(any(UUID.class), eq(request.getDisplayName()), eq(ManagedDocumentType.TEMPLATE))).thenReturn(NEW_DOCUMENT_UUID);
        when(templateRepository.findActiveTemplateByCaseType( request.getCaseType())).thenReturn(null);

        templateService.createTemplate(request);

        verify(documentClient,times(1)).createDocument(any(UUID.class),eq(DISPLAY_NAME), eq(ManagedDocumentType.TEMPLATE));
        verify(templateRepository, times(1)).findActiveTemplateByCaseType(request.getCaseType());
        verify(templateRepository, times(1)).save(any());
        verify(documentClient).processDocument(ManagedDocumentType.TEMPLATE, NEW_DOCUMENT_UUID, "URL");
        verifyNoMoreInteractions(templateRepository);
        verifyNoMoreInteractions(documentClient);
    }

    @Test
    public void shouldCreateStandardLineExpiringPrevious(){
        CreateTemplateDocumentDto request = new CreateTemplateDocumentDto(DISPLAY_NAME, CASE_TYPE,"URL");

        when(documentClient.createDocument(any(UUID.class), eq(request.getDisplayName()), eq(ManagedDocumentType.TEMPLATE))).thenReturn(NEW_DOCUMENT_UUID);
        when(templateRepository.findActiveTemplateByCaseType( request.getCaseType())).thenReturn(new Template(DISPLAY_NAME, CASE_TYPE));

        templateService.createTemplate(request);

        verify(documentClient,times(1)).createDocument(any(UUID.class), eq(DISPLAY_NAME), eq(ManagedDocumentType.TEMPLATE));
        verify(templateRepository, times(1)).findActiveTemplateByCaseType(request.getCaseType());
        verify(templateRepository, times(2)).save(any());
        verify(documentClient).deleteDocument(any(UUID.class));
        verify(documentClient).processDocument(ManagedDocumentType.TEMPLATE, NEW_DOCUMENT_UUID, "URL");
        verifyNoMoreInteractions(templateRepository);
        verifyNoMoreInteractions(documentClient);
    }

    @Test(expected = ApplicationExceptions.EntityNotFoundException.class)
    public void shouldNotGetCaseWithValidParamsNotFoundException() {
        String caseType = CASE_TYPE;

        when(templateRepository.findActiveTemplateByCaseType(caseType)).thenReturn(null);

        templateService.getTemplateForCaseType(caseType);
    }

}