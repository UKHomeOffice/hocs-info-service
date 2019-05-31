package uk.gov.digital.ho.hocs.info.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.gov.digital.ho.hocs.info.api.dto.UnitDto;

import java.util.Set;
import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@Slf4j
@RestController
public class UnitResource {

    private final UnitService unitService;

    @Autowired
    public UnitResource(UnitService unitService) {
        this.unitService = unitService;
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

    @DeleteMapping(value = "/unit/{unitUUID}", produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity deleteUnit(@PathVariable UUID unitUUID) {
        unitService.deleteUnit(unitUUID);
        return ResponseEntity.ok().build();
    }
}
