package uk.gov.digital.ho.hocs.info.caseType;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.digital.ho.hocs.info.dto.CaseTypeDto;
import uk.gov.digital.ho.hocs.info.dto.GetCaseTypesResponse;
import uk.gov.digital.ho.hocs.info.exception.EntityNotFoundException;

import java.util.Arrays;
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

    @RequestMapping(value = "/getcasetypes", method = RequestMethod.GET, produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<GetCaseTypesResponse> getAllCaseTypes(@RequestHeader("X-Auth-Roles") String[] roles) {
        if (roles.length > 0) {
            log.info("requesting all case types for Tenants {}", Arrays.toString(roles));
            try {
                List<CaseTypeDto> caseTypes = caseTypeService.getCaseTypes(Arrays.asList(roles));
                return ResponseEntity.ok(new GetCaseTypesResponse(caseTypes));
            } catch (EntityNotFoundException e) {
                return ResponseEntity.badRequest().build();
            }
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
}
