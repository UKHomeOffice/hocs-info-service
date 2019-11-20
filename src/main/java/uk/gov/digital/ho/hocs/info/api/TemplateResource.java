package uk.gov.digital.ho.hocs.info.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.gov.digital.ho.hocs.info.api.dto.CreateTemplateDocumentDto;
import uk.gov.digital.ho.hocs.info.api.dto.TemplateDto;
import uk.gov.digital.ho.hocs.info.domain.model.Template;

import java.util.List;
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

    @GetMapping(value = "/templates", produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<TemplateDto>> getTemplates() {
        List<Template> templates = templateService.getActiveTemplates();
        return ResponseEntity.ok(templates.stream().map(TemplateDto::from).collect(Collectors.toList()));
    }

    @GetMapping(value = "/template/{uuid}", produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<TemplateDto> getTemplate(@PathVariable UUID uuid) {
        return ResponseEntity.ok(TemplateDto.from(templateService.getTemplate(uuid)));
    }

    @DeleteMapping(value = "/template/{uuid}", produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity deleteTemplate(@PathVariable UUID uuid) {
        templateService.deleteTemplate(uuid);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/caseType/{caseType}/templates", produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<TemplateDto>> getTemplatesForCaseType(@PathVariable String caseType) {
        List<Template> templates = templateService.getTemplatesForCaseType(caseType);
        return ResponseEntity.ok(templates.stream().map(TemplateDto::from).collect(Collectors.toList()));
    }

    @PostMapping(value = "/template", produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity createTemplate(@RequestBody CreateTemplateDocumentDto request) {
        templateService.createTemplate(request);
        return ResponseEntity.ok().build();
    }

}
