package uk.gov.digital.ho.hocs.info.unit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.gov.digital.ho.hocs.info.dto.GetUnitsResponse;
import uk.gov.digital.ho.hocs.info.dto.UnitDto;
import uk.gov.digital.ho.hocs.info.entities.Unit;
import uk.gov.digital.ho.hocs.info.exception.EntityPermissionException;

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

    @GetMapping(value = "/unit/casetype/{casetype}", produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Set<UnitDto>> getAllUnitsForCaseType(@PathVariable String caseType) {
            Set<UnitDto> units = unitService.getAllUnitsForCaseType(caseType);
            return ResponseEntity.ok(units);
    }

    @GetMapping(value = "/unit", produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Set<UnitDto>> getAllUnits() {
        Set<UnitDto> units = unitService.getAllUnits();
        return ResponseEntity.ok(units);
    }

    @PostMapping(value = "/unit", produces= APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity createUnit(@RequestBody UnitDto unit) {
        unitService.createUnit(unit);
        return ResponseEntity.ok().build();
    }
}
