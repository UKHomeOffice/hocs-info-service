package uk.gov.digital.ho.hocs.info.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.digital.ho.hocs.info.api.dto.FieldDto;
import uk.gov.digital.ho.hocs.info.api.dto.SchemaDto;
import uk.gov.digital.ho.hocs.info.domain.model.Field;
import uk.gov.digital.ho.hocs.info.domain.model.Schema;

import java.io.IOException;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
public class SchemaResource {

    private final SchemaService schemaService;

    @Autowired
    public SchemaResource(SchemaService schemaService) {
        this.schemaService = schemaService;
    }

    @GetMapping(value = "/schema/{type}", produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SchemaDto> getSchema(@PathVariable String type) {
        Schema schema = schemaService.getSchemaByType(type);
        return ResponseEntity.ok(SchemaDto.from(schema));
    }

    @GetMapping(value = "/schema/caseType/{caseType}", produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Set<SchemaDto>> getAllSchemasForCaseType(@PathVariable String caseType) {
        Set<Schema> schemas = schemaService.getAllSchemasForCaseType(caseType);
        return ResponseEntity.ok(schemas.stream().map(SchemaDto::from).collect(Collectors.toSet()));
    }

    @GetMapping(value = "/schema/caseType/{caseType}/summary", produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<FieldDto>> getAllSummaryFieldsForCaseType(@PathVariable String caseType) {
        List<Field> fields = schemaService.getAllSummaryFieldsForCaseType(caseType);
        return ResponseEntity.ok(fields.stream().map(FieldDto::from).collect(Collectors.toList()));
    }

    @GetMapping(value = "/schema/caseType/{caseType}/reporting", produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Set<String>> getAllReportingFieldsForCaseType(@PathVariable String caseType) {
        Stream<Field> fields = Stream.concat(schemaService.getExtractOnlyFields().stream(), schemaService.getAllReportingFieldsForCaseType(caseType).sorted(Comparator.comparingLong(Field::getId)));

        return ResponseEntity.ok(fields.map(Field::getName).collect(Collectors.toCollection(LinkedHashSet::new)));
    }

}