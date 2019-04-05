package uk.gov.digital.ho.hocs.info.domain.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uk.gov.digital.ho.hocs.info.domain.model.Schema;

import java.util.Set;

@Repository
public interface SchemaRepository extends CrudRepository<Schema, String> {

    @Query(value = "SELECT ss.*, fi.*, cts.stage_type FROM screen_schema ss JOIN field_screen fs ON ss.uuid = fs.schema_uuid JOIN field fi on fs.field_uuid = fi.uuid JOIN case_type_schema cts ON ss.uuid = cts.schema_uuid AND cts.case_type = ?1 AND ss.active = TRUE", nativeQuery = true)
    Set<Schema> findAllActiveFormsByCaseType(String caseType);

    Set<Schema> findAll();

    @Query(value = "SELECT ss.*, '' as stage_type FROM screen_schema ss WHERE ss.type = ?1 AND ss.active = TRUE", nativeQuery = true)
    Schema findByType(String type);
}