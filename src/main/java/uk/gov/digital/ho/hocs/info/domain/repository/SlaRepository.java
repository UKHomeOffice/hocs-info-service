package uk.gov.digital.ho.hocs.info.domain.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uk.gov.digital.ho.hocs.info.domain.model.Sla;

import java.util.Set;

@Repository
public interface SlaRepository extends CrudRepository<Sla, String> {

    @Query(value ="SELECT s.* FROM sla s WHERE s.case_type = ?1" , nativeQuery = true )
    Set<Sla> findAllByCaseType(String caseType);

    @Query(value ="SELECT s.* FROM sla s WHERE s.stage_type = ?1" , nativeQuery = true )
    Sla findAllByStageType(String stageType);

    @Query(value ="SELECT s.* FROM sla s JOIN case_type c ON c.case_deadline = s.stage_type WHERE c.type = ?1" , nativeQuery = true )
    Sla findCaseDeadlineSlaByCaseType(String caseType);

}
