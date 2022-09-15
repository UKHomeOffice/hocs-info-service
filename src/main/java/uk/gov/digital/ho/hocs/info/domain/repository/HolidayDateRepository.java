package uk.gov.digital.ho.hocs.info.domain.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uk.gov.digital.ho.hocs.info.domain.model.ExemptionDate;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Repository
public interface HolidayDateRepository extends CrudRepository<ExemptionDate, String> {

    @Query(value = "SELECT ed.* FROM exemption_date ed WHERE ed.case_type_uuid = ?1", nativeQuery = true)
    Set<ExemptionDate> findAllByCaseType(UUID caseTypeUUID);

    @Query(value = "select ed.* from exemption_date ed join stage_type s on ed.case_type_uuid = s.case_type_uuid where s.type = ?1",
           nativeQuery = true)
    Set<ExemptionDate> findAllByStageType(String stageType);

    @Query(value = "SELECT ed.* FROM exemption_date ed join case_type ct on ed.case_type_uuid = ct.uuid where ct.type = ?1",
           nativeQuery = true)
    List<ExemptionDate> findAllByCaseType(String caseType);

}
