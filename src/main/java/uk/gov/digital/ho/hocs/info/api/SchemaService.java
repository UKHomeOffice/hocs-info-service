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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SchemaService {

    private final FieldRepository fieldRepository;

    private final SchemaRepository schemaRepository;

    private final ObjectMapper mapper;

    @Autowired
    public SchemaService(FieldRepository fieldRepository, SchemaRepository schemaRepository, ObjectMapper mapper) {
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
    List<Field> getAllSummaryFieldsForCaseType(String caseType) {
        log.debug("Getting all summary fields CaseType {}", caseType);
        List<Field> summaryFields = fieldRepository.findAllSummaryFields(caseType);
        log.info("Got {} summary fields for CaseType {}", summaryFields.size(), caseType);
        return summaryFields;
    }

    @Deprecated(forRemoval = true)
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
        return fields.stream().map(field -> FieldDto.fromWithDecoratedProps(field, mapper)).collect(
            Collectors.toList());
    }

}
