package uk.gov.digital.ho.hocs.info.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.gov.digital.ho.hocs.info.api.dto.SomuTypeDto;
import uk.gov.digital.ho.hocs.info.domain.model.SomuType;

import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
public class SomuTypeResource {

    private final SomuTypeService somuTypeService;

    @Autowired
    public SomuTypeResource(SomuTypeService somuTypeService) {
        this.somuTypeService = somuTypeService;
    }

    @GetMapping(value = "/somuType", produces = APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity<Set<SomuTypeDto>> getAllSomuTypes() {
        Set<SomuType> stageTypes = somuTypeService.getAllSomuTypes();
        return ResponseEntity.ok(stageTypes.stream().map(SomuTypeDto::from).collect(Collectors.toSet()));
    }

    @GetMapping(value = "/somuType/{caseType}/{type}", produces = APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity<SomuTypeDto> getSomuTypeForCaseTypeAndType(@PathVariable String caseType, @PathVariable String type) {
        SomuType somuType = somuTypeService.getSomuTypeForCaseTypeAndType(caseType, type);
        return ResponseEntity.ok(SomuTypeDto.from(somuType));
    }

    @PostMapping(value = "/somuType/{caseType}/{type}", produces = APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity<SomuTypeDto> upsertSomuTypeForCaseTypeAndType(@PathVariable String caseType, @PathVariable String type, @RequestBody String schema) {
        SomuType somuType = somuTypeService.upsertSomuTypeForCaseTypeAndType(caseType, type, schema);
        return ResponseEntity.ok(SomuTypeDto.from(somuType));
    }

    @DeleteMapping(value = "/somuType/{caseType}/{type}", produces = APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity deleteSomuTypeForCaseTypeAndType(@PathVariable String caseType, @PathVariable String type) {
        somuTypeService.deleteSomuTypeForCaseTypeAndType(caseType, type);
        return ResponseEntity.ok().build();
    }
}
