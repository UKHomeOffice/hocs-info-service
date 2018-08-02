package uk.gov.digital.ho.hocs.info.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uk.gov.digital.ho.hocs.info.entities.Sla;

import java.util.Set;

@Repository
public interface SlaRepository extends CrudRepository<Sla, String> {

    @Query(value ="SELECT s.* FROM sla s WHERE s.case_type = ?1" , nativeQuery = true )
    Set<Sla> findAllByCaseType(String caseType);

}
