package uk.gov.digital.ho.hocs.info.template;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.digital.ho.hocs.info.entities.Template;
import uk.gov.digital.ho.hocs.info.repositories.TemplateRepository;

@Service
@Slf4j
public class TemplateService {

    private final TemplateRepository templateRepository;

    @Autowired
    public TemplateService(TemplateRepository templateRepository) {
        this.templateRepository = templateRepository;
    }

    public Template getTemplate(String caseType){
        log.info("Requesting template for case type {} ", caseType);
        return templateRepository.findActiveTemplateByCaseType(caseType);

    }
}
