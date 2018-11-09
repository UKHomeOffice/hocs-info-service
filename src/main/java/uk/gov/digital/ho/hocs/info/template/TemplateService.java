package uk.gov.digital.ho.hocs.info.template;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class TemplateService {

    private final TemplateRepository templateRepository;
    private final DocumentClient documentClient;
    private final UUID TEMPLATE_EXTERNAL_REFERENCE_UUID = UUID.fromString("88888888-8888-8888-8888-888888888888");


    @Autowired
    public TemplateService(TemplateRepository templateRepository,
                           DocumentClient documentClient) {
        this.templateRepository = templateRepository;
        this.documentClient = documentClient;
    }

    public Template getTemplates(String caseType){
        Template template = templateRepository.findActiveTemplateByCaseType(caseType);
        if(template != null){
            log.info("Got Template for CaseType {} ", caseType);
        return template;
        } else {
            throw new EntityNotFoundException("Template for CaseType: %s, not found!", caseType);
        }
    }

    void createTemplateDocument(CreateTemplateDocumentDto document) {

        if (document != null) {
            UUID templateUUID = documentClient.createDocument(TEMPLATE_EXTERNAL_REFERENCE_UUID, document.getDisplayName(), ManagedDocumentType.TEMPLATE);

            setDeletedExistingTemplateIfExists(document);

            saveTemplate(document, templateUUID);

            documentClient.processDocument(ManagedDocumentType.TEMPLATE, templateUUID, document.getS3UntrustedUrl());
        } else {
            throw new EntityCreationException("no template");

        }
    }

    private void setDeletedExistingTemplateIfExists(CreateTemplateDocumentDto document) {
        Template template = templateRepository.findActiveTemplateByCaseType(document.getCaseType());
        if (template != null) {
            template.delete();
            templateRepository.save(template);
            deleteDocument(TEMPLATE_EXTERNAL_REFERENCE_UUID, template.getUuid());
            log.info("Set Deleted to True for Template - {}, id {}", template.getDisplayName(), template.getUuid());
        }
    }

    private void saveTemplate(CreateTemplateDocumentDto document, UUID templateUUID) {
        Template newTemplate = new Template(document.getDisplayName(), document.getCaseType(), templateUUID);
        templateRepository.save(newTemplate);
    }

    private void deleteDocument(UUID externalReferenceUUID, UUID documentUUID) {
        documentClient.deleteDocument(externalReferenceUUID, documentUUID);
    }
}
