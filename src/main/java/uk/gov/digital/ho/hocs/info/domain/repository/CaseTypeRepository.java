package uk.gov.digital.ho.hocs.info.domain.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uk.gov.digital.ho.hocs.info.domain.model.CaseType;

import java.util.List;
import java.util.Set;

@Repository
public interface CaseTypeRepository extends CrudRepository<CaseType, String> {

    // Behaviour is to list both cases with false initialCaseType
    @Query(value = "SELECT ct.* FROM case_type ct JOIN permission ON ct.type = permission.case_type WHERE permission.team_uuid IN ?1 AND permission.access_level != 'MIGRATE' AND ct.active AND (ct.initial_case OR ct.initial_case = ?2)", nativeQuery = true)
    Set<CaseType> findAllCaseTypesByTeam(Set<String> team, boolean initialCaseType);

    // Behaviour is to list both cases with false initialCaseType
    @Query(value = "SELECT ct.* FROM case_type ct JOIN info.permission ON ct.type = permission.case_type WHERE permission.team_uuid IN ?1 AND permission.access_level != 'MIGRATE' AND ct.active AND ct.bulk AND (ct.initial_case OR ct.initial_case = ?2)", nativeQuery = true)
    Set<CaseType> findAllBulkCaseTypesByTeam(Set<String> team, boolean initialCaseType);

    @Query(value = "select ct.id, " +
            "ct.uuid, " +
            "ct.display_name, " +
            "ct.short_code, " +
            "ct.type, " +
            "ct.owning_unit_uuid, " +
            "ct.deadline_stage," +
            "ct.bulk," +
            "ct.active," +
            "ct.previous_case_type " +
            "from case_type ct " +
            "where (ct.previous_case_type IS NULL OR (ct.previous_case_type IS NOT NULL) = ?1)", nativeQuery=true)
    Set<CaseType> findByIncludePreviousCaseType(Boolean addCaseTypeWithPreviousCase);

    CaseType findByShortCode(String shortCode);

    @Query(value = "SELECT ctab.tab_name FROM case_type_tab ctt JOIN case_type ctype on ctt.case_type_uuid = ctype.uuid JOIN case_tab ctab on ctt.case_tab_uuid = ctab.uuid WHERE ctype.type = ?1", nativeQuery=true)
    List<String> findTabsByType(String type);

    CaseType findByType(String type);

}
