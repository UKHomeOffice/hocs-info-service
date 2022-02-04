package uk.gov.digital.ho.hocs.info.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
public class CaseTypeBankHolidayRegionResource {
    CaseTypeBankHolidayRegionService caseTypeBankHolidayRegionService;

    @Autowired
    public CaseTypeBankHolidayRegionResource(CaseTypeBankHolidayRegionService caseTypeBankHolidayRegionService) {
        this.caseTypeBankHolidayRegionService = caseTypeBankHolidayRegionService;
    }

    @GetMapping(value = "/bankHolidayRegion/caseTypeUuid/{caseTypeUuid}", produces = APPLICATION_JSON_VALUE)
    public List<String> getBankHolidayRegionsByCaseType(@PathVariable UUID caseTypeUuid) {
        return caseTypeBankHolidayRegionService.getBankHolidayRegionsByCaseType(caseTypeUuid);
    }
}
