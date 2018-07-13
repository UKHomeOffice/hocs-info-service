package uk.gov.digital.ho.hocs.info.Repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import uk.gov.digital.ho.hocs.info.entities.CaseType;
import uk.gov.digital.ho.hocs.info.entities.SLA;

import java.util.List;

public interface SLARepository extends CrudRepository<SLA, Long> {

    @Query(value ="select * from sla s join case_type c on s.case_type_id = c.id where c.display_name = ?1", nativeQuery = true )
    List<SLA> findSLACaseType(String caseType);



}
