package uk.gov.digital.ho.hocs.info.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uk.gov.digital.ho.hocs.info.entities.Sla;

import java.util.List;

@Repository
public interface SlaRepository extends CrudRepository<Sla, Long> {

    @Query(value ="select * from sla s join case_type c on s.case_type_id = c.id where c.type = ?1", nativeQuery = true )
    List<Sla> findSLACaseType(String caseType);



}
