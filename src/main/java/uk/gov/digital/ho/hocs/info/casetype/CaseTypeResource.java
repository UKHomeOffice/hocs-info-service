package uk.gov.digital.ho.hocs.info.casetype;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.digital.ho.hocs.info.dto.CaseTypeDto;
import uk.gov.digital.ho.hocs.info.dto.GetCaseTypesResponse;
import java.util.Set;
import uk.gov.digital.ho.hocs.info.entities.CaseTypeEntity;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
public class CaseTypeResource {

    private final CaseTypeService caseTypeService;

    @Autowired
    public CaseTypeResource(CaseTypeService caseTypeService) {
        this.caseTypeService = caseTypeService;
    }

    @GetMapping(value = "/casetype/single", produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<GetCaseTypesResponse> getCaseTypesSingle() {
        Set<CaseTypeEntity> caseTypes = caseTypeService.getCaseTypes();
        return ResponseEntity.ok(GetCaseTypesResponse.from(caseTypes));
    }

    @GetMapping(value = "/casetype/bulk", produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<GetCaseTypesResponse> getCaseTypesBulk() {
        Set<CaseTypeEntity> caseTypes = caseTypeService.getCaseTypesBulk();
        return ResponseEntity.ok(GetCaseTypesResponse.from(caseTypes));
    }

    @GetMapping(value = "/caseType", produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<GetCaseTypesResponse> getCaseTypes() {
        Set<CaseTypeEntity> caseTypes = caseTypeService.getAllCaseTypes();
        return ResponseEntity.ok(GetCaseTypesResponse.from(caseTypes));
    }

    @GetMapping(value = "/caseType/shortCode/{shortCode}", produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<CaseTypeDto> getCaseTypeByShortcode(@PathVariable String shortCode) {
        CaseTypeEntity caseType = caseTypeService.getCaseTypeByShortCode(shortCode);
        return ResponseEntity.ok(CaseTypeDto.from(caseType));
    }
}
