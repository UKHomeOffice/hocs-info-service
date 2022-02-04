package uk.gov.digital.ho.hocs.info.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.digital.ho.hocs.info.domain.model.CaseTypeBankHolidayRegion;
import uk.gov.digital.ho.hocs.info.domain.repository.CaseTypeBankHolidayRegionRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CaseTypeBankHolidayRegionService {
    CaseTypeBankHolidayRegionRepository caseTypeBankHolidayRegionRepository;

    @Autowired
    public CaseTypeBankHolidayRegionService(CaseTypeBankHolidayRegionRepository caseTypeBankHolidayRegionRepository) {
        this.caseTypeBankHolidayRegionRepository = caseTypeBankHolidayRegionRepository;
    }

    public List<String> getBankHolidayRegionsByCaseType(UUID uuid) {
        log.debug("Getting Bank Holiday Regions for case type {}", uuid);

        final List<String> bankHolidayRegionsForCaseType = caseTypeBankHolidayRegionRepository
                .findAllByCaseTypeUuid(uuid)
                .stream()
                .map(CaseTypeBankHolidayRegion::getRegion)
                .map(Enum::toString)
                .collect(Collectors.toList());

        log.debug("Got {} Bank Holiday Regions for case type {}", bankHolidayRegionsForCaseType.size(), uuid);

        return bankHolidayRegionsForCaseType;
    }
}
