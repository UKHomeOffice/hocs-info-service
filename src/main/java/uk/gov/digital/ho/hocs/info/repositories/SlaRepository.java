package uk.gov.digital.ho.hocs.info.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uk.gov.digital.ho.hocs.info.entities.Sla;

import java.util.Set;

@Repository
public interface SlaRepository extends CrudRepository<Sla, Long> {

    @Query(value ="select s.* from sla s join case_type c on s.case_type = c.type where c.type = ?1 and c.tenant_role in (?2)" , nativeQuery = true )
    Set<Sla> findSLACaseType(String caseType, String[] roles);

}
