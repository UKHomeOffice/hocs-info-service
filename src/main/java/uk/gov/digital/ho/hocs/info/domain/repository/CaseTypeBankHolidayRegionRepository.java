package uk.gov.digital.ho.hocs.info.domain.repository;

import org.springframework.data.repository.CrudRepository;
import uk.gov.digital.ho.hocs.info.domain.model.CaseTypeBankHolidayRegion;

import java.util.List;
import java.util.UUID;

public interface CaseTypeBankHolidayRegionRepository extends CrudRepository<CaseTypeBankHolidayRegion, UUID> {

    List<CaseTypeBankHolidayRegion> findAllByCaseTypeUuid(UUID caseTypeUuid);

}
