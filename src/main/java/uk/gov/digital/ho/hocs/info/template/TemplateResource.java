package uk.gov.digital.ho.hocs.info.template;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.digital.ho.hocs.info.dto.GetTemplateKeyResponse;
import uk.gov.digital.ho.hocs.info.dto.GetTemplateResponse;
import uk.gov.digital.ho.hocs.info.entities.Template;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
public class TemplateResource {

    private final TemplateService templateService;

    @Autowired
    public TemplateResource(TemplateService templateService) {
        this.templateService = templateService;
    }

    @GetMapping(value = "/casetype/{caseType}/templates", produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<GetTemplateResponse> getTemplatesForCaseType(@PathVariable String caseType) {
            List<Template> template = templateService.getTemplates(caseType);
            return ResponseEntity.ok(GetTemplateResponse.from(template));
    }

    @GetMapping(value = "/casetype/{caseType}/template/{templateUUID}", produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<GetTemplateKeyResponse> getTemplateKey(@PathVariable String caseType, @PathVariable UUID templateUUID) {
        Template template = templateService.getTemplateKey(templateUUID);
        return ResponseEntity.ok(GetTemplateKeyResponse.from(template));
    }

}
