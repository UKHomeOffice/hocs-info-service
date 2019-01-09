package uk.gov.digital.ho.hocs.info.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.digital.ho.hocs.info.api.dto.FieldDto;
import uk.gov.digital.ho.hocs.info.api.dto.FormDto;
import uk.gov.digital.ho.hocs.info.domain.model.Field;
import uk.gov.digital.ho.hocs.info.domain.model.Form;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
public class FormResource {

    private final FormService formService;

    @Autowired
    public FormResource(FormService formService) {
        this.formService = formService;
    }

    @GetMapping(value = "/form/{formType}", produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<FormDto> getForm(@PathVariable String formType) {
        Form form = formService.getFormByType(formType);
        return ResponseEntity.ok(FormDto.from(form));
    }

    @GetMapping(value = "/form/caseType/{caseType}", produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Set<FormDto>> getFormForCaseType(@PathVariable String caseType) {
        Set<Form> form = formService.getAllFormsForCaseType(caseType);
        return ResponseEntity.ok(form.stream().map(FormDto::from).collect(Collectors.toSet()));
    }

    @GetMapping(value = "/form/caseType/{caseType}/summary", produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Set<FieldDto>> getFormForCaseTypeSummary(@PathVariable String caseType) {
        Stream<Field> fields = formService.getAllFormsForCaseTypeSummary(caseType);
        return ResponseEntity.ok(fields.map(FieldDto::from).collect(Collectors.toSet()));
    }

}