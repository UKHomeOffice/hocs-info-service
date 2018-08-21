package uk.gov.digital.ho.hocs.info.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uk.gov.digital.ho.hocs.info.entities.CaseTypeEntity;

import java.util.Set;

@Repository
public interface CaseTypeRepository extends CrudRepository<CaseTypeEntity, String> {

    // This is used as a permission check, if we return null here you do not have permissions on that case type
    @Query(value = "SELECT ct.type FROM case_type ct WHERE ct.tenant_role IN (?2) AND ct.type = ?1 AND ct.active = TRUE", nativeQuery = true)
    String findCaseTypeEntityByTenant(String caseType, String[] tenant);

    @Query(value = "SELECT ct.* FROM case_type ct WHERE ct.tenant_role IN (?1) AND ct.active = TRUE", nativeQuery = true)
    Set<CaseTypeEntity> findAllCaseTypesByTenant(String[] tenant);

    @Query(value = "SELECT ct.* FROM case_type ct WHERE ct.tenant_role IN (?1) AND ct.active = TRUE AND ct.bulk = TRUE", nativeQuery = true)
    Set<CaseTypeEntity> findAllBulkCaseTypesByTenant(String[] tenant);

}