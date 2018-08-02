package uk.gov.digital.ho.hocs.info.unit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.gov.digital.ho.hocs.info.dto.GetMembersResponse;
import uk.gov.digital.ho.hocs.info.dto.GetUnitsResponse;
import uk.gov.digital.ho.hocs.info.entities.Member;
import uk.gov.digital.ho.hocs.info.entities.Unit;
import uk.gov.digital.ho.hocs.info.exception.EntityNotFoundException;
import uk.gov.digital.ho.hocs.info.exception.EntityPermissionException;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@Slf4j
@RestController
public class UnitResource {

    private final UnitService unitService;

    @Autowired
    public UnitResource(UnitService unitService) {
        this.unitService = unitService;
    }

    @RequestMapping(value = "/casetype/{caseType}/unit", method = RequestMethod.GET, produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<GetUnitsResponse> getAllUnits(@PathVariable String caseType) {
        try {
            Set<Unit> units = unitService.getActiveUnitsByCaseType(caseType);
            return ResponseEntity.ok(GetUnitsResponse.from(units));
        } catch ( EntityPermissionException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build() ;
        }
    }
}
