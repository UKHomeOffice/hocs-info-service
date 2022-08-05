package uk.gov.digital.ho.hocs.info.domain.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import uk.gov.digital.ho.hocs.info.domain.model.Field;
import uk.gov.digital.ho.hocs.info.security.AccessLevel;

import java.util.List;

public interface FieldRepository extends CrudRepository<Field, String> {

    @Query(value = "SELECT * FROM field fi WHERE fi.summary = TRUE AND fi.uuid IN (SELECT field_uuid FROM field_screen fs INNER JOIN screen_schema ss ON ss.uuid = fs.schema_uuid INNER JOIN case_type_schema cts ON cts.schema_uuid = ss.uuid WHERE fs.field_uuid = fi.uuid AND ss.active = TRUE AND cts.case_type = ?1) ORDER BY fi.id", nativeQuery = true)
    List<Field> findAllSummaryFields(String caseType);

    @Deprecated(forRemoval = true)
    @Query(value = "SELECT * from field f LEFT JOIN field_screen fs ON f.uuid = fs.field_uuid LEFT JOIN screen_schema ss ON ss.uuid = fs.schema_uuid WHERE ss.type = ?1 ORDER BY sort_order ASC", nativeQuery = true)
    List<Field> findAllBySchemaType(String schemaType);

    @Deprecated(forRemoval = true)
    @Query(value = "SELECT * FROM field fi WHERE fi.access_level = ?2 AND fi.uuid IN (SELECT field_uuid FROM field_screen fs INNER JOIN screen_schema ss ON ss.uuid = fs.schema_uuid INNER JOIN case_type_schema cts ON cts.schema_uuid = ss.uuid WHERE fs.field_uuid = fi.uuid AND ss.active = TRUE AND cts.case_type = ?1) ORDER BY fi.id", nativeQuery = true)
    List<Field> findAllByAccessLevelAndCaseType(String caseType, String accessLevel);
}
