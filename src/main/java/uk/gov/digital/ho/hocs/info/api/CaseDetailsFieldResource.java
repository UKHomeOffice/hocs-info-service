package uk.gov.digital.ho.hocs.info.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.digital.ho.hocs.info.api.dto.CaseDetailsFieldDto;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@Deprecated(forRemoval = true)
@RestController
@Slf4j
public class CaseDetailsFieldResource {

    private final CaseDetailsFieldService caseDetailsFieldService;

    @Autowired
    public CaseDetailsFieldResource(CaseDetailsFieldService caseDetailsFieldService) {
        this.caseDetailsFieldService = caseDetailsFieldService;
    }

    @GetMapping(value = "/caseDetailsFields/{caseType}", produces = APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity<List<CaseDetailsFieldDto>> getCaseDetailsFieldsByCaseType(@PathVariable String caseType) {
        log.debug("Getting CaseDetailsFieldDtos for {}", caseType);
        List<CaseDetailsFieldDto> caseDetailsFieldDtos = caseDetailsFieldService.getCaseDetailsFieldsByCaseType(
            caseType);
        log.debug("Got {} CaseDetailsFieldDtos for {}", caseDetailsFieldDtos.size(), caseType);
        return ResponseEntity.ok(caseDetailsFieldDtos);
    }

}
