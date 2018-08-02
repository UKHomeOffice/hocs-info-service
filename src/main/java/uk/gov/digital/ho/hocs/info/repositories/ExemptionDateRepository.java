package uk.gov.digital.ho.hocs.info.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uk.gov.digital.ho.hocs.info.entities.ExemptionDate;

import java.time.LocalDate;
import java.util.Set;

@Repository
public interface ExemptionDateRepository extends CrudRepository<ExemptionDate, String> {

    @Query(value ="SELECT ed.date FROM exemption_date ed WHERE ed.case_type = ?1", nativeQuery = true )
    Set<LocalDate> findAllByCaseType(String caseType);

}
