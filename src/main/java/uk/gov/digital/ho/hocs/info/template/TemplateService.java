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
import uk.gov.digital.ho.hocs.info.exception.EntityPermissionException;
import uk.gov.digital.ho.hocs.info.repositories.TemplateRepository;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class TemplateService {

    private final TemplateRepository templateRepository;
    private final CaseTypeService caseTypeService;
    private final RequestData requestData;
    private final DocumentClient documentClient;
    private final UUID TEMPLATE_EXTERNAL_REFERENCE_UUID = UUID.fromString("88888888-8888-8888-8888-888888888888");


    @Autowired
    public TemplateService(TemplateRepository templateRepository,
                           CaseTypeService caseTypeService,
                           RequestData requestData,
                           DocumentClient documentClient) {
        this.templateRepository = templateRepository;
        this.caseTypeService = caseTypeService;
        this.requestData = requestData;
        this.documentClient = documentClient;
    }

    public List<Template> getTemplates(String caseType) throws EntityPermissionException {
        log.info("Requesting template for case type {} ", caseType);
        if (caseTypeService.hasPermissionForCaseType(caseType)) {
            return templateRepository.findActiveTemplateByCaseType(caseType);
        } else {
            //TODO AUDIT permission exception
            throw new EntityPermissionException("Not allowed to get Units for CaseType, CaseType: %s not in Roles: %s", caseType, requestData.rolesString());
        }
    }

    void createTemplateDocument(CreateTemplateDocumentDto document) throws EntityCreationException {

        if (document != null) {
            UUID templateUUID = documentClient.createDocument(TEMPLATE_EXTERNAL_REFERENCE_UUID, document.getDisplayName(), ManagedDocumentType.TEMPLATE);

            Template template = templateRepository.findTemplateByDisplayNameAndCaseType(document.getDisplayName(), document.getCaseType());

            if (template != null) {
                template.delete();
                templateRepository.save(template);
                deleteDocument(TEMPLATE_EXTERNAL_REFERENCE_UUID, template.getUuid());
                log.info("Set Deleted to True for Template - {}, id {}", template.getDisplayName(), template.getUuid());
            }

            Template newTemplate = new Template(document.getDisplayName(), document.getCaseType(), templateUUID);
            templateRepository.save(newTemplate);


            documentClient.processDocument(ManagedDocumentType.TEMPLATE, templateUUID, document.getS3UntrustedUrl());
        }
    }

    void deleteDocument(UUID externalReferenceUUID, UUID documentUUID) throws EntityCreationException {
        documentClient.deleteDocument(externalReferenceUUID, documentUUID);
    }
}
