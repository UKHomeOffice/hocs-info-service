package uk.gov.digital.ho.hocs.info.casetype;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.digital.ho.hocs.info.dto.GetCaseTypesResponse;
import uk.gov.digital.ho.hocs.info.entities.CaseTypeEntity;

import java.util.Set;

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
}
