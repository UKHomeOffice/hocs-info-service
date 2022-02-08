package uk.gov.digital.ho.hocs.info.domain.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import uk.gov.digital.ho.hocs.info.domain.model.Field;
import uk.gov.digital.ho.hocs.info.security.AccessLevel;

import java.util.List;

public interface FieldRepository extends CrudRepository<Field, String> {

    @Query(value = "SELECT * FROM field fi WHERE fi.summary = TRUE AND fi.uuid IN (SELECT field_uuid FROM field_screen fs INNER JOIN screen_schema ss ON ss.uuid = fs.schema_uuid INNER JOIN case_type_schema cts ON cts.schema_uuid = ss.uuid WHERE fs.field_uuid = fi.uuid AND ss.active = TRUE AND cts.case_type = ?1) ORDER BY fi.id", nativeQuery = true)
    List<Field> findAllSummaryFields(String caseType);

    List<Field> findAllByActiveIsTrueAndAccessLevelIs(AccessLevel accessLevel);
}
