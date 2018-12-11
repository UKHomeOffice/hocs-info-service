package uk.gov.digital.ho.hocs.info.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.Set;
import uk.gov.digital.ho.hocs.info.entities.CaseTypeEntity;



@Repository
public interface CaseTypeRepository extends CrudRepository<CaseTypeEntity, String> {

    @Query(value = "SELECT ct.* FROM case_type ct JOIN permission ON ct.type = permission.case_type WHERE permission.team_uuid IN ?1 AND ct.active = TRUE", nativeQuery = true)
    Set<CaseTypeEntity> findAllCaseTypesByTeam(Set<String> team);

    //TODO: Remove Tenant (link to team instead)
    @Query(value = "SELECT ct.* FROM case_type ct JOIN permission ON ct.type = permission.case_type WHERE permission.team_uuid IN ?1 AND ct.active = TRUE AND ct.bulk = TRUE", nativeQuery = true)
    Set<CaseTypeEntity> findAllBulkCaseTypesByTeam(Set<String> team);

    Set<CaseTypeEntity> findAllBy();

    CaseTypeEntity findByShortCode(String shortCode);

    CaseTypeEntity findByType(String type);
}