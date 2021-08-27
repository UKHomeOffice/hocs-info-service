package uk.gov.digital.ho.hocs.info.domain.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uk.gov.digital.ho.hocs.info.domain.model.CaseType;

import java.util.Set;

@Repository
public interface CaseTypeRepository extends CrudRepository<CaseType, String> {

    // Behaviour is to list both cases with NULL previousCaseType + any NOT NULL when asked for
    @Query(value = "SELECT ct.* FROM case_type ct JOIN permission ON ct.type = permission.case_type WHERE permission.team_uuid IN ?1 AND ct.active AND (ct.previous_case_type IS NULL OR (ct.previous_case_type IS NOT NULL) = ?2)", nativeQuery = true)
    Set<CaseType> findAllCaseTypesByTeam(Set<String> team, boolean includePreviousCaseType);

    // Behaviour is to list both cases with NULL previousCaseType + any NOT NULL when asked for
    @Query(value = "SELECT ct.* FROM case_type ct JOIN permission ON ct.type = permission.case_type WHERE permission.team_uuid IN ?1 AND ct.active AND ct.bulk AND (ct.previous_case_type IS NULL OR (ct.previous_case_type IS NOT NULL) = ?2)", nativeQuery = true)
    Set<CaseType> findAllBulkCaseTypesByTeam(Set<String> team, boolean includePreviousCaseType);

    Set<CaseType> findByPreviousCaseTypeIsNull();

    CaseType findByShortCode(String shortCode);

    CaseType findByType(String type);

}