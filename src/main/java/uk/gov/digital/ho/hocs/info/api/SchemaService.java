package uk.gov.digital.ho.hocs.info.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.digital.ho.hocs.info.domain.exception.ApplicationExceptions;
import uk.gov.digital.ho.hocs.info.domain.model.*;
import uk.gov.digital.ho.hocs.info.domain.repository.SchemaRepository;

import java.util.Set;
import java.util.stream.Stream;

@Service
@Slf4j
public class SchemaService {

    private final SchemaRepository schemaRepository;

    @Autowired
    public SchemaService(SchemaRepository schemaRepository) {
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

    Stream<Field> getAllSummaryFieldsForCaseType(String caseType) {
        Set<Schema> caseTypeSchemas = getAllSchemasForCaseType(caseType);
        log.debug("Filtering to summary only.");
        return caseTypeSchemas.stream().flatMap(f -> f.getFields().stream()).filter(Field::isSummary);
    }

    public Stream<Field> getAllReportingFieldsForCaseType(String caseType) {
        Set<Schema> caseTypeSchemas = getAllSchemasForCaseType(caseType);
        log.debug("Filtering to reporting only.");
        return caseTypeSchemas.stream().flatMap(f -> f.getFields().stream()).filter(Field::isReporting);
    }
}
