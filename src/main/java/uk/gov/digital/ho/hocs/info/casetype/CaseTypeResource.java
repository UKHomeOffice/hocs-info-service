package uk.gov.digital.ho.hocs.info.casetype;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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

    @RequestMapping(value = "/casetype", method = RequestMethod.GET, produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<GetCaseTypesResponse> getCaseTypes() {
        Set<CaseTypeEntity> caseTypes = caseTypeService.getCaseTypes();
        return ResponseEntity.ok(GetCaseTypesResponse.from(caseTypes));
    }
}
