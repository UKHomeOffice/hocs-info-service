package uk.gov.digital.ho.hocs.info.template;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.digital.ho.hocs.info.entities.Template;
import uk.gov.digital.ho.hocs.info.repositories.TemplateRepository;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class TemplateService {

    private final TemplateRepository templateRepository;

    @Autowired
    public TemplateService(TemplateRepository templateRepository) {
        this.templateRepository = templateRepository;
    }

    public List<Template> getTemplates(String caseType){
        log.info("Requesting template for case type {} ", caseType);
        return templateRepository.findActiveTemplateByCaseType(caseType);

    }

    public Template getTemplateKey(UUID templateUUID) {
        log.info("Requesting template key for template {} ", templateUUID);
        return templateRepository.findTemplateByUuid(templateUUID);
    }
}
