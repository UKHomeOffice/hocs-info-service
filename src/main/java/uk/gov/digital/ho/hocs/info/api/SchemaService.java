package uk.gov.digital.ho.hocs.info.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.digital.ho.hocs.info.api.dto.FieldDto;
import uk.gov.digital.ho.hocs.info.domain.exception.ApplicationExceptions;
import uk.gov.digital.ho.hocs.info.domain.model.Field;
import uk.gov.digital.ho.hocs.info.domain.model.Schema;
import uk.gov.digital.ho.hocs.info.domain.repository.FieldRepository;
import uk.gov.digital.ho.hocs.info.domain.repository.SchemaRepository;
import uk.gov.digital.ho.hocs.info.security.AccessLevel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
public class SchemaService {

    private final FieldRepository fieldRepository;
    private final SchemaRepository schemaRepository;
    private final ObjectMapper mapper;

    @Autowired
    public SchemaService(
            FieldRepository fieldRepository,
            SchemaRepository schemaRepository,
            ObjectMapper mapper
    ) {
        this.fieldRepository = fieldRepository;
        this.schemaRepository = schemaRepository;
        this.mapper = mapper;
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

    @Deprecated(forRemoval = true)
    Set<Schema> getAllSchemasForCaseType(String caseType) {
        log.debug("Getting all Forms for CaseType {}", caseType);
        Set<Schema> caseTypeSchemas = schemaRepository.findAllActiveFormsByCaseType(caseType);
        log.info("Got {} Forms for CaseType {}", caseTypeSchemas.size(), caseType);
        return caseTypeSchemas;
    }

    @Deprecated(forRemoval = true)
    List<Field> getAllSummaryFieldsForCaseType(String caseType) {
        log.debug("Getting all summary fields CaseType {}", caseType);
        List<Field> summaryFields = fieldRepository.findAllSummaryFields(caseType);
        log.info("Got {} summary fields for CaseType {}", summaryFields.size(), caseType);
        return summaryFields;
    }

    @Deprecated(forRemoval = true)
    public Stream<Field> getAllReportingFieldsForCaseType(String caseType) {
        Set<Schema> caseTypeSchemas = getAllSchemasForCaseType(caseType);
        log.debug("Filtering to reporting only.");
        return caseTypeSchemas.stream().flatMap(f -> f.getFields().stream()).filter(Field::isReporting);
    }

    @Deprecated(forRemoval = true)
    public List<Field> getExtractOnlyFields() {
        Schema extractOnlySchema = schemaRepository.findExtractOnlySchema();

        if(extractOnlySchema != null){
            log.debug("Getting extract only fields.");
            return extractOnlySchema.getFields().stream().filter(Field::isReporting).collect(Collectors.toList());
        }

        return new ArrayList<>();
    }

    Set<Schema> getAllSchemasForCaseTypeAndStage(String caseType, String stages) {
        log.debug("Getting all Forms for stages {} and CaseType {}", stages, caseType);
        List<String> stagesList = new ArrayList<>(Arrays.asList(stages.split(",")));
        Set<Schema> caseTypeSchemas = schemaRepository.findAllActiveFormsByCaseTypeAndStages(caseType, stagesList);
        log.info("Got {} Forms for CaseType {} and stages {}", caseTypeSchemas.size(), caseType, stages);
        return caseTypeSchemas;
    }

    @Deprecated(forRemoval = true)
    public List<FieldDto> getFieldsBySchemaType(String schemaType) {
        log.debug("Getting all Fields for schema {}", schemaType);
        List<Field> fields = fieldRepository.findAllBySchemaType(schemaType);
        log.debug("Got {} Fields for Schema {}", fields.size(), schemaType);
        return fields.stream().map(field -> FieldDto.fromWithDecoratedProps(field, mapper)).collect(Collectors.toList());
    }

    @Deprecated(forRemoval = true)
    public List<FieldDto> getFieldsByCaseTypePermissionLevel(String caseType, AccessLevel accessLevel) {

        log.debug("Requesting all fields with access permissions of {} for caseType {}", accessLevel, caseType);
        List<Field> fields = fieldRepository.findAllByAccessLevelAndCaseType(caseType, accessLevel.toString());
        log.info("Returning {} fields with accessLevel {} for caseType {}", fields.size(), accessLevel, caseType);
        return fields.stream().map(FieldDto::from).collect(Collectors.toList());
    }
}
