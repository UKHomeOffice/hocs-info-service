package uk.gov.digital.ho.hocs.info.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uk.gov.digital.ho.hocs.info.entities.CaseTypeEntity;

import java.util.Set;

@Repository
public interface CaseTypeRepository extends CrudRepository<CaseTypeEntity, Long> {

    @Query(value = "SELECT ct.* FROM case_type ct WHERE ct.type = ?1 AND ct.active = TRUE AND ct.tenant_role IN (?2);", nativeQuery = true)
    CaseTypeEntity findCaseTypeEntityByTenant(String caseType, String[] tenant);

    @Query(value = "SELECT c.* FROM case_type c WHERE c.active = TRUE AND c.tenant_role IN (?1)", nativeQuery = true)
    Set<CaseTypeEntity> findAllCaseTypesByTenant(String[] tenant);

}