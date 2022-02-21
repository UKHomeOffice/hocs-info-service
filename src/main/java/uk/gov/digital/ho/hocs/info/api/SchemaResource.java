package uk.gov.digital.ho.hocs.info.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.digital.ho.hocs.info.api.dto.FieldDto;
import uk.gov.digital.ho.hocs.info.api.dto.SchemaDto;
import uk.gov.digital.ho.hocs.info.domain.model.Field;
import uk.gov.digital.ho.hocs.info.domain.model.Schema;
import uk.gov.digital.ho.hocs.info.security.AccessLevel;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
public class SchemaResource {

    private final SchemaService schemaService;
    private final ExtractService extractService;

    @Autowired
    public SchemaResource(SchemaService schemaService,
                          ExtractService extractService) {
        this.schemaService = schemaService;
        this.extractService = extractService;
    }

    @GetMapping(value = "/schema/{type}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<SchemaDto> getSchema(@PathVariable String type) {
        Schema schema = schemaService.getSchemaByType(type);
        final SchemaDto from = SchemaDto.from(schema);
        return ResponseEntity.ok(from);
    }

    @GetMapping(value = "/schema/caseType/{caseType}", params = {"stages"}, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SchemaDto>> getAllSchemasForCaseType(
            @PathVariable String caseType,
            @RequestParam("stages") String stages
    ) {
        Set<Schema> schemas = schemaService.getAllSchemasForCaseTypeAndStage(caseType, stages);
        return ResponseEntity.ok(schemas.stream().map(SchemaDto::from).collect(Collectors.toList()));
    }

    @GetMapping(value = "/schema/caseType/{caseType}/summary", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<FieldDto>> getAllSummaryFieldsForCaseType(@PathVariable String caseType) {
        List<Field> fields = schemaService.getAllSummaryFieldsForCaseType(caseType);
        return ResponseEntity.ok(fields.stream().map(FieldDto::from).collect(Collectors.toList()));
    }

    @GetMapping(value = "/schema/caseType/{caseType}/reporting", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<String>> getAllReportingFieldsForCaseType(@PathVariable String caseType) {
        List<String> fields = extractService.getAllReportingFieldsForCaseType(caseType);
        return ResponseEntity.ok(fields);
    }

    @GetMapping(value = "/schema/{schemaType}/fields", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<FieldDto>> getFieldsBySchemaType(@PathVariable String schemaType) {
        List<FieldDto> fields = schemaService.getFieldsBySchemaType(schemaType);
        return ResponseEntity.ok(fields);
    }

    @GetMapping(value = "/schema/caseType/{caseType}/permission/{accessLevel}/fields", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<FieldDto>> getFieldsByPermissionLevel(@PathVariable String caseType, @PathVariable String accessLevel) {
        AccessLevel requiredLevel = AccessLevel.valueOf(accessLevel);
        List<FieldDto> restrictedFieldList = schemaService.getFieldsByPermissionLevel(requiredLevel, caseType);
        return ResponseEntity.ok(restrictedFieldList);
    }
}
