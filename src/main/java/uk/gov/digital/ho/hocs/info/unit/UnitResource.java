package uk.gov.digital.ho.hocs.info.unit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.digital.ho.hocs.info.dto.GetUnitsResponse;
import uk.gov.digital.ho.hocs.info.entities.Unit;
import uk.gov.digital.ho.hocs.info.exception.EntityNotFoundException;

import java.util.Arrays;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@Slf4j
@RestController
public class UnitResource {

    private final UnitService unitService;

    @Autowired
    public UnitResource(UnitService unitService) {
        this.unitService = unitService;
    }

    @RequestMapping(value = "/units", method = RequestMethod.GET, produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<GetUnitsResponse> getAllUnits(@RequestHeader("X-Auth-Roles") String[] roles) {

        if (roles.length > 0) {
            log.info("requesting all units for Tenants {}", Arrays.toString(roles));
            //try {
                List<Unit> units = unitService.getUnits(Arrays.asList(roles));
                return ResponseEntity.ok(new GetUnitsResponse(units));
          //  } catch (EntityNotFoundException e) {
           //     return ResponseEntity.badRequest().build();
          //  }
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
}
