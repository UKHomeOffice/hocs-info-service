package uk.gov.digital.ho.hocs.info.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.gov.digital.ho.hocs.info.api.dto.CreateTemplateDocumentDto;
import uk.gov.digital.ho.hocs.info.api.dto.GetStandardLineResponse;
import uk.gov.digital.ho.hocs.info.api.dto.GetTemplateResponse;
import uk.gov.digital.ho.hocs.info.domain.model.Template;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
public class TemplateResource {

    private final TemplateService templateService;

    @Autowired
    public TemplateResource(TemplateService templateService) {
        this.templateService = templateService;
    }

    @GetMapping(value = "/template", produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Set<GetTemplateResponse>> getTemplates() {
        Set<Template> templates = templateService.getActiveTemplates();
        return ResponseEntity.ok(templates.stream().map(GetTemplateResponse::from).collect(Collectors.toSet()));
    }

    @GetMapping(value = "/caseType/{caseType}/template", produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<GetTemplateResponse> getTemplatesForCaseType(@PathVariable String caseType) {
        Template template = templateService.getTemplateForCaseType(caseType);
        return ResponseEntity.ok(GetTemplateResponse.from(template));
    }

    @PostMapping(value = "/template", produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity createTemplate(@RequestBody CreateTemplateDocumentDto request) {
        templateService.createTemplate(request);
        return ResponseEntity.ok().build();
    }

}
