package uk.gov.digital.ho.hocs.info.caseType;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.digital.ho.hocs.info.dto.CaseTypeDto;
import uk.gov.digital.ho.hocs.info.dto.GetCaseTypesRequest;
import uk.gov.digital.ho.hocs.info.dto.GetCaseTypesResponse;
import uk.gov.digital.ho.hocs.info.exception.EntityNotFoundException;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@Slf4j
@RestController
public class CaseTypeResource {

    private final CaseTypeService caseTypeService;

    @Autowired
    public CaseTypeResource(CaseTypeService caseTypeService) {
        this.caseTypeService = caseTypeService;
    }

    @RequestMapping(value = "/getcasetypes", method = RequestMethod.POST, produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<GetCaseTypesResponse> getAllCaseTypes(@RequestBody GetCaseTypesRequest getCaseTypesRequest){
        log.info("requesting all case types for Tenants {}", getCaseTypesRequest.getTenants());
        try {
            List<CaseTypeDto> caseTypes = caseTypeService.getCaseTypes(getCaseTypesRequest.getTenants());
            return ResponseEntity.ok(new GetCaseTypesResponse(caseTypes));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
