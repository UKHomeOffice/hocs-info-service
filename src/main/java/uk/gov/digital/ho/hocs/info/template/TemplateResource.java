package uk.gov.digital.ho.hocs.info.template;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.gov.digital.ho.hocs.info.dto.CreateTemplateDocumentDto;
import uk.gov.digital.ho.hocs.info.dto.GetTemplateResponse;
import uk.gov.digital.ho.hocs.info.entities.Template;
import uk.gov.digital.ho.hocs.info.exception.EntityPermissionException;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
public class TemplateResource {

    private final TemplateService templateService;

    @Autowired
    public TemplateResource(TemplateService templateService) {
        this.templateService = templateService;
    }

    @GetMapping(value = "/templates/{caseType}", produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<GetTemplateResponse> getTemplatesForCaseType(@PathVariable String caseType) {
        Template template = templateService.getTemplates(caseType);
        return ResponseEntity.ok(GetTemplateResponse.from(template));

    }

    @PostMapping(value = "template/document", produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity createDocument(@RequestBody CreateTemplateDocumentDto request) {
        templateService.createTemplateDocument(request);
        return ResponseEntity.ok().build();
    }

}
