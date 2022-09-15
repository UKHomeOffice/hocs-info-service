package uk.gov.digital.ho.hocs.info.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.digital.ho.hocs.info.api.dto.CreateTemplateDocumentDto;
import uk.gov.digital.ho.hocs.info.client.caseworkclient.CaseworkClient;
import uk.gov.digital.ho.hocs.info.client.documentclient.DocumentClient;
import uk.gov.digital.ho.hocs.info.client.documentclient.model.ManagedDocumentType;
import uk.gov.digital.ho.hocs.info.domain.exception.ApplicationExceptions;
import uk.gov.digital.ho.hocs.info.domain.model.Template;
import uk.gov.digital.ho.hocs.info.domain.repository.TemplateRepository;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class TemplateService {

    private final TemplateRepository templateRepository;

    private final DocumentClient documentClient;

    private final CaseworkClient caseworkClient;

    @Autowired
    public TemplateService(TemplateRepository templateRepository,
                           DocumentClient documentClient,
                           CaseworkClient caseworkClient) {
        this.templateRepository = templateRepository;
        this.documentClient = documentClient;
        this.caseworkClient = caseworkClient;
    }

    public void createTemplate(CreateTemplateDocumentDto request) {
        log.debug("Creating Template {} for CaseType {} ", request.getDisplayName(), request.getCaseType());

        Template newTemplate = new Template(request.getDisplayName(), request.getCaseType());
        UUID templateDocumentUUID = documentClient.createDocument(newTemplate.getUuid(), request.getDisplayName(),
            request.getS3UntrustedUrl(), ManagedDocumentType.TEMPLATE);
        newTemplate.setDocumentUUID(templateDocumentUUID);
        templateRepository.save(newTemplate);

        caseworkClient.clearCachedTemplateForCaseType(request.getCaseType());

        log.info("Created Template {} for CaseType {} with uuid {} ", request.getDisplayName(), request.getCaseType(),
            newTemplate.getUuid());
    }

    public void deleteTemplate(UUID uuid) {

        Template template = templateRepository.findActiveTemplateByUuid(uuid);

        if (template != null) {
            template.delete();
            templateRepository.save(template);
            documentClient.deleteDocument(template.getDocumentUUID());
            log.info("Set Deleted to True for Template - {}, id {}", template.getDisplayName(), template.getUuid());
            caseworkClient.clearCachedTemplateForCaseType(template.getCaseType());
        }
    }

    public List<Template> getActiveTemplates() {
        List<Template> templates = templateRepository.findActiveTemplates();
        log.info("Got {} Templates", templates.size());
        return templates;
    }

    public List<Template> getTemplatesForCaseType(String caseType) {
        List<Template> templates = templateRepository.findActiveTemplatesByCaseType(caseType);
        log.info("Got {} Templates for CaseType {} ", templates.size(), caseType);
        return templates;
    }

    public Template getTemplate(UUID uuid) {
        Template template = templateRepository.findActiveTemplateByUuid(uuid);
        if (template != null) {
            log.info("Got Template for uuid {} ", uuid);
            return template;
        } else {
            throw new ApplicationExceptions.EntityNotFoundException("Template for uuid: %s, not found!", uuid);
        }
    }

}
