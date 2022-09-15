package uk.gov.digital.ho.hocs.info.domain.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uk.gov.digital.ho.hocs.info.domain.model.CaseTab;

import java.util.List;

@Repository
@Deprecated(forRemoval = true)
public interface CaseTabRepository extends CrudRepository<CaseTab, String> {

    @Query(value = "SELECT ctab.uuid, ctab.tab_name, ctab.tab_label, ctab.tab_screen FROM case_type_tab ctt JOIN case_type ctype on ctt.case_type_uuid = ctype.uuid JOIN case_tab ctab on ctt.case_tab_uuid = ctab.uuid WHERE ctype.type = ?1 ORDER BY ctt.sort_order",
           nativeQuery = true)
    List<CaseTab> findTabsByType(String type);

}
