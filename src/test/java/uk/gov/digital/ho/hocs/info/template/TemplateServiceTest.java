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
    private DocumentClient documentClient;

    private TemplateService templateService;
    private static final UUID TEMPLATE_EXT_REF = UUID.fromString("88888888-8888-8888-8888-888888888888");
    private static final UUID DOCUMENT_UUID = UUID.randomUUID();
    private static final UUID NEW_DOCUMENT_UUID = UUID.randomUUID();
    private static final String DISPLAY_NAME = "dn";

    @Before
    public void setUp() {
        this.templateService = new TemplateService(templateRepository,documentClient);
    }

    @Test
    public void shouldReturnListOfTemplates() throws EntityPermissionException {
        templateService.getTemplates("MIN");
        verify(templateRepository, times(1)).findActiveTemplateByCaseType(any());
        verifyNoMoreInteractions(templateRepository);
    }

    @Test
    public void shouldCreateNewTemplate(){
        CreateTemplateDocumentDto request = new CreateTemplateDocumentDto(DISPLAY_NAME,"MIN","URL");

        when(documentClient.createDocument(TEMPLATE_EXT_REF, request.getDisplayName(), ManagedDocumentType.TEMPLATE)).thenReturn(NEW_DOCUMENT_UUID);
        when(templateRepository.findTemplateByDisplayNameAndCaseType(request.getDisplayName() , request.getCaseType())).thenReturn(null);

        templateService.createTemplateDocument(request);

        verify(documentClient,times(1)).createDocument(TEMPLATE_EXT_REF, DISPLAY_NAME,ManagedDocumentType.TEMPLATE );
        verify(templateRepository, times(1)).findTemplateByDisplayNameAndCaseType(request.getDisplayName(),request.getCaseType());
        verify(templateRepository, times(1)).save(any());
        verify(documentClient).processDocument(ManagedDocumentType.TEMPLATE, NEW_DOCUMENT_UUID, "URL");
        verifyNoMoreInteractions(templateRepository);
        verifyNoMoreInteractions(documentClient);
    }

    @Test
    public void shouldCreateStandardLineExpiringPrevious(){
        CreateTemplateDocumentDto request = new CreateTemplateDocumentDto(DISPLAY_NAME,"MIN","URL");

        when(documentClient.createDocument(TEMPLATE_EXT_REF, request.getDisplayName(), ManagedDocumentType.TEMPLATE)).thenReturn(NEW_DOCUMENT_UUID);
        when(templateRepository.findTemplateByDisplayNameAndCaseType(request.getDisplayName() , request.getCaseType())).thenReturn(new Template(DISPLAY_NAME, "MIN", DOCUMENT_UUID));

        templateService.createTemplateDocument(request);

        verify(documentClient,times(1)).createDocument(TEMPLATE_EXT_REF, DISPLAY_NAME,ManagedDocumentType.TEMPLATE );
        verify(templateRepository, times(1)).findTemplateByDisplayNameAndCaseType(request.getDisplayName(),request.getCaseType());
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

}
