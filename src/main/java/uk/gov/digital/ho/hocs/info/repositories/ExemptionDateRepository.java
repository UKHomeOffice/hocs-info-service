package uk.gov.digital.ho.hocs.info.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uk.gov.digital.ho.hocs.info.entities.ExemptionDate;

import java.time.LocalDate;
import java.util.Set;

@Repository
public interface ExemptionDateRepository extends CrudRepository<ExemptionDate, Long> {

    @Query(value ="select e.date from exemption_date e join case_type ct on e.tenant_role = ct.tenant_role where ct.type = ?1 and e.tenant_role in (?2);", nativeQuery = true )
    Set<LocalDate> findAllByCaseType(String caseTypeDisplayName, String[] tenant);

}
