package uk.gov.digital.ho.hocs.info.domain.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uk.gov.digital.ho.hocs.info.domain.model.CaseTypeSchema;

import java.util.List;

@Repository
public interface CaseTypeSchemaRepository extends CrudRepository<CaseTypeSchema, String> {

    @Query(value = "SELECT DISTINCT(stage_type) FROM case_type_schema WHERE case_type = ?1", nativeQuery = true)
    List<String> findDistinctStagesByCaseType(String caseType);

}
