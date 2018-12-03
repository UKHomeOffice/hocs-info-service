package uk.gov.digital.ho.hocs.info.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.Set;
import uk.gov.digital.ho.hocs.info.entities.CaseTypeEntity;



@Repository
public interface CaseTypeRepository extends CrudRepository<CaseTypeEntity, String> {

    //TODO: Remove Tenant (link to team instead)
    // This is used as a permission check, if we return null here you do not have permissions on that case type
    @Query(value = "SELECT ct.type FROM case_type ct WHERE ct.tenant_role IN (?2) AND ct.type = ?1 AND ct.active = TRUE", nativeQuery = true)
    String findCaseTypeEntityByTenant(String caseType, String[] tenant);

    //TODO: Remove Tenant (link to team instead)
    @Query(value = "SELECT ct.* FROM case_type ct WHERE ct.tenant_role IN (?1) AND ct.active = TRUE", nativeQuery = true)
    Set<CaseTypeEntity> findAllCaseTypesByTenant(String[] tenant);

    //TODO: Remove Tenant (link to team instead)
    @Query(value = "SELECT ct.* FROM case_type ct WHERE ct.tenant_role IN (?1) AND ct.active = TRUE AND ct.bulk = TRUE", nativeQuery = true)
    Set<CaseTypeEntity> findAllBulkCaseTypesByTenant(String[] tenant);

    Set<CaseTypeEntity> findAllBy();

    CaseTypeEntity findByShortCode(String shortCode);

    CaseTypeEntity findByType(String type);
}