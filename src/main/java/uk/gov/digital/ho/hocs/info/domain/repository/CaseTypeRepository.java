package uk.gov.digital.ho.hocs.info.domain.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uk.gov.digital.ho.hocs.info.domain.model.CaseType;

import java.util.Set;

@Repository
public interface CaseTypeRepository extends CrudRepository<CaseType, String> {

    @Query(value = "SELECT ct.* FROM case_type ct JOIN permission ON ct.type = permission.case_type WHERE permission.team_uuid IN ?1 AND ct.active = TRUE", nativeQuery = true)
    Set<CaseType> findAllCaseTypesByTeam(Set<String> team);

    @Query(value = "SELECT ct.* FROM case_type ct JOIN permission ON ct.type = permission.case_type WHERE permission.team_uuid IN ?1 AND ct.active = TRUE AND ct.bulk = TRUE", nativeQuery = true)
    Set<CaseType> findAllBulkCaseTypesByTeam(Set<String> team);

    Set<CaseType> findAll();

    CaseType findByShortCode(String shortCode);

    CaseType findByType(String type);

}