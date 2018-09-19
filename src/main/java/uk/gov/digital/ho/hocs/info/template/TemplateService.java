package uk.gov.digital.ho.hocs.info.template;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.digital.ho.hocs.info.RequestData;
import uk.gov.digital.ho.hocs.info.casetype.CaseTypeService;
import uk.gov.digital.ho.hocs.info.entities.Template;
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

    @Autowired
    public TemplateService(TemplateRepository templateRepository, CaseTypeService caseTypeService, RequestData requestData) {
        this.templateRepository = templateRepository;
        this.caseTypeService = caseTypeService;
        this.requestData = requestData;
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

    public Template getTemplateKey(UUID templateUUID) {
        log.info("Requesting template key for template {} ", templateUUID);
        return templateRepository.findTemplateByUuid(templateUUID);
    }
}
