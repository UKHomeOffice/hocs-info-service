package uk.gov.digital.ho.hocs.info.template;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.digital.ho.hocs.info.dto.GetTemplateResponse;
import uk.gov.digital.ho.hocs.info.entities.Template;
import uk.gov.digital.ho.hocs.info.exception.EntityPermissionException;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
public class TemplateResource {

    private final TemplateService templateService;

    @Autowired
    public TemplateResource(TemplateService templateService) {
        this.templateService = templateService;
    }

    @GetMapping(value = "/casetype/{caseType}/template", produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<GetTemplateResponse> getTemplateForCaseTypes(@PathVariable String caseType) {
        try {
            Template template = templateService.getTemplate(caseType);
            return ResponseEntity.ok(GetTemplateResponse.from(template));
        } catch (EntityPermissionException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

}
