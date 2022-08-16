package uk.gov.digital.ho.hocs.info.domain.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uk.gov.digital.ho.hocs.info.domain.model.Schema;

import java.util.List;
import java.util.Set;

@Repository
public interface SchemaRepository extends CrudRepository<Schema, String> {

    @Deprecated(forRemoval = true)
    @Query(value = "SELECT ss.*, fi.*, cts.stage_type FROM screen_schema ss JOIN field_screen fs ON ss.uuid = fs.schema_uuid JOIN field fi on fs.field_uuid = fi.uuid JOIN case_type_schema cts ON ss.uuid = cts.schema_uuid AND cts.case_type = ?1 AND ss.active = TRUE", nativeQuery = true)
    Set<Schema> findAllActiveFormsByCaseType(String caseType);

    Set<Schema> findAll();

    @Query(value = "SELECT ss.*, '' as stage_type FROM screen_schema ss WHERE ss.type = ?1 AND ss.active = TRUE", nativeQuery = true)
    Schema findByType(String type);

    @Deprecated(forRemoval = true)
    @Query(value = "SELECT ss.*, '' as stage_type FROM screen_schema ss WHERE ss.type = 'EXTRACT_ONLY'", nativeQuery = true)
    Schema findExtractOnlySchema();

    @Query(value = "SELECT ss.*, fi.*, cts.stage_type FROM screen_schema ss " +
            "JOIN field_screen fs ON ss.uuid = fs.schema_uuid JOIN field fi on fs.field_uuid = fi.uuid " +
            "JOIN case_type_schema cts ON ss.uuid = cts.schema_uuid AND cts.case_type = ?1 AND ss.active = TRUE " +
            "JOIN stage_type st ON cts.stage_type = st.type " +
            "WHERE st.type IN (?2) " +
            "AND st.can_display_stage = true " +
            "ORDER BY st.display_stage_order",
            nativeQuery = true )
    Set<Schema> findAllActiveFormsByCaseTypeAndStages(String caseType,  List<String> stages);

}
