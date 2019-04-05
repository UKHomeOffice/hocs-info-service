package uk.gov.digital.ho.hocs.info.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.digital.ho.hocs.info.client.documentClient.DocumentClient;
import uk.gov.digital.ho.hocs.info.client.documentClient.model.ManagedDocumentType;
import uk.gov.digital.ho.hocs.info.api.dto.CreateTemplateDocumentDto;
import uk.gov.digital.ho.hocs.info.domain.exception.ApplicationExceptions;
import uk.gov.digital.ho.hocs.info.domain.model.Template;
import uk.gov.digital.ho.hocs.info.domain.repository.TemplateRepository;

import java.util.Set;
import java.util.UUID;

@Service
@Slf4j
public class TemplateService {

    private final TemplateRepository templateRepository;
    private final DocumentClient documentClient;

    @Autowired
    public TemplateService(TemplateRepository templateRepository,
                           DocumentClient documentClient) {
        this.templateRepository = templateRepository;
        this.documentClient = documentClient;
    }

    void createTemplate(CreateTemplateDocumentDto request) {
        log.debug("Creating Template {} for CaseType {} ", request.getDisplayName(), request.getCaseType());
        Template template = templateRepository.findActiveTemplateByCaseType(request.getCaseType());

        if (template != null) {
            template.delete();
            templateRepository.save(template);
            documentClient.deleteDocument(template.getUuid());
            log.info("Set Deleted to True for Template - {}, id {}", template.getDisplayName(), template.getUuid());
        }

        Template newTemplate = new Template(request.getDisplayName(), request.getCaseType());
        templateRepository.save(newTemplate);

        UUID templateDocumentUUID = documentClient.createDocument(newTemplate.getUuid(), request.getDisplayName(), ManagedDocumentType.TEMPLATE);

        documentClient.processDocument(ManagedDocumentType.TEMPLATE, templateDocumentUUID, request.getS3UntrustedUrl());
        log.info("Created Template {} for CaseType {} ", request.getDisplayName(), request.getCaseType());
    }

    Set<Template> getActiveTemplates() {
        Set<Template> templates = templateRepository.findActiveTemplates();
        log.info("Got {} Templates", templates.size());
        return templates;
    }

    Template getTemplateForCaseType(String caseType) {
        Template template = templateRepository.findActiveTemplateByCaseType(caseType);
        if (template != null) {
            log.info("Got Template for CaseType {} ", caseType);
            return template;
        } else {
            throw new ApplicationExceptions.EntityNotFoundException("Template for CaseType: %s, not found!", caseType);
        }
    }

}
