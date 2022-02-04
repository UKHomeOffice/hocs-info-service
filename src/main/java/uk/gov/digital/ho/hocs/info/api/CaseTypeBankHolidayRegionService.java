package uk.gov.digital.ho.hocs.info.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.digital.ho.hocs.info.domain.model.CaseTypeBankHolidayRegion;
import uk.gov.digital.ho.hocs.info.domain.repository.CaseTypeBankHolidayRegionRepository;
import uk.gov.digital.ho.hocs.info.domain.repository.CaseTypeRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CaseTypeBankHolidayRegionService {
    CaseTypeBankHolidayRegionRepository caseTypeBankHolidayRegionRepository;
    CaseTypeRepository caseTypeRepository;

    @Autowired
    public CaseTypeBankHolidayRegionService(
            CaseTypeBankHolidayRegionRepository caseTypeBankHolidayRegionRepository,
            CaseTypeRepository caseTypeRepository) {
        this.caseTypeBankHolidayRegionRepository = caseTypeBankHolidayRegionRepository;
        this.caseTypeRepository = caseTypeRepository;
    }

    public List<String> getBankHolidayRegionsByCaseType(String caseType) {
        log.debug("Getting Bank Holiday Regions for case type {}", caseType);

        UUID uuid = caseTypeRepository.findByType(caseType).getUuid();

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
