package uk.gov.digital.ho.hocs.info.domain.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uk.gov.digital.ho.hocs.info.domain.model.Schema;

import java.util.Set;

@Repository
public interface SchemaRepository extends CrudRepository<Schema, String> {

    @Query(value = "SELECT fo.*, fi.* FROM screen_schema fo JOIN field fi ON fo.uuid = fi.schema_uuid JOIN case_type_schema fct ON fo.uuid = fct.schema_uuid AND fct.case_type = ?1 AND fo.active = TRUE", nativeQuery = true)
    Set<Schema> findAllActiveFormsByCaseType(String caseType);
    Set<Schema> findAll();
    Schema findByType(String type);
}