package uk.gov.digital.ho.hocs.info.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.digital.ho.hocs.info.domain.exception.ApplicationExceptions;
import uk.gov.digital.ho.hocs.info.domain.model.Field;
import uk.gov.digital.ho.hocs.info.domain.model.Schema;
import uk.gov.digital.ho.hocs.info.domain.repository.FieldRepository;
import uk.gov.digital.ho.hocs.info.domain.repository.SchemaRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
public class SchemaService {

    private final FieldRepository fieldRepository;
    private final SchemaRepository schemaRepository;

    @Autowired
    public SchemaService(
            FieldRepository fieldRepository,
            SchemaRepository schemaRepository) {
        this.fieldRepository = fieldRepository;
        this.schemaRepository = schemaRepository;
    }

    Schema getSchemaByType(String type) {
        log.debug("Getting Schema for type {}", type);
        Schema schema = schemaRepository.findByType(type);
        if (schema != null) {
            log.info("Got Schema {} for type {}", schema.getUuid(), type);
            return schema;
        } else {
            throw new ApplicationExceptions.EntityNotFoundException("Schema for type %s was not found", type);
        }
    }

    Set<Schema> getAllSchemasForCaseType(String caseType) {
        log.debug("Getting all Forms for CaseType {}", caseType);
        Set<Schema> caseTypeSchemas = schemaRepository.findAllActiveFormsByCaseType(caseType);
        log.info("Got {} Forms for CaseType {}", caseTypeSchemas.size(), caseType);
        return caseTypeSchemas;
    }

    List<Field> getAllSummaryFieldsForCaseType(String caseType) {
        log.debug("Getting all summary fields CaseType {}", caseType);
        List<Field> summaryFields = fieldRepository.findAllSummaryFields(caseType);
        log.info("Got {} summary fields for CaseType {}", summaryFields.size(), caseType);
        return summaryFields;
    }

    public Stream<Field> getAllReportingFieldsForCaseType(String caseType) {
        Set<Schema> caseTypeSchemas = getAllSchemasForCaseType(caseType);
        log.debug("Filtering to reporting only.");
        return caseTypeSchemas.stream().flatMap(f -> f.getFields().stream()).filter(Field::isReporting);
    }

    public List<Field> getExtractOnlyFields() {
        Schema extractOnlySchema = schemaRepository.findExtractOnlySchema();

        if(extractOnlySchema != null){
            log.debug("Getting extract only fields.");
            return extractOnlySchema.getFields().stream().filter(Field::isReporting).collect(Collectors.toList());
        }

        return new ArrayList<>();
    }
}
