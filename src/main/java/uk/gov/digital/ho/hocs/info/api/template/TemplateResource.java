package uk.gov.digital.ho.hocs.info.api.template;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.gov.digital.ho.hocs.info.api.dto.CreateTemplateDocumentDto;
import uk.gov.digital.ho.hocs.info.api.dto.GetTemplateResponse;
import uk.gov.digital.ho.hocs.info.domain.model.Template;

import java.util.UUID;

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

    @GetMapping(value = "/case/{caseUUID}/templates", produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<GetTemplateResponse> getTemplate(@PathVariable UUID caseUUID) {
        Template template =  templateService.getTemplateList(caseUUID);
        return ResponseEntity.ok(GetTemplateResponse.from(template));
    }

    @PostMapping(value = "template/document", produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity createDocument(@RequestBody CreateTemplateDocumentDto request) {
        templateService.createTemplateDocument(request);
        return ResponseEntity.ok().build();
    }

}
