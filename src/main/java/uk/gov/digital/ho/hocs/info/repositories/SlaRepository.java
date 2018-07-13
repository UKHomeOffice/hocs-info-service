package uk.gov.digital.ho.hocs.info.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import uk.gov.digital.ho.hocs.info.entities.Sla;

import java.util.List;

public interface SlaRepository extends CrudRepository<Sla, Long> {

    @Query(value ="select * from sla s join case_type c on s.case_type_id = c.id where c.display_name = ?1", nativeQuery = true )
    List<Sla> findSLACaseType(String caseType);



}
