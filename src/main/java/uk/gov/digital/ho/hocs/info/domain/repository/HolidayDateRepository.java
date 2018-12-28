package uk.gov.digital.ho.hocs.info.domain.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uk.gov.digital.ho.hocs.info.domain.model.HolidayDate;

import java.util.Set;

@Repository
public interface HolidayDateRepository extends CrudRepository<HolidayDate, String> {

    @Query(value ="SELECT ed.* FROM holiday_date ed WHERE ed.case_type = ?1", nativeQuery = true )
    Set<HolidayDate> findAllByCaseType(String caseType);

    @Query(value ="select h.* from holiday_date h join sla s on h.case_type = s.case_type where s.stage_type = ?1", nativeQuery = true )
    Set<HolidayDate> findAllByStageType(String stageType);
}
