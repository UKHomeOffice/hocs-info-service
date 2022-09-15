package uk.gov.digital.ho.hocs.info.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.gov.digital.ho.hocs.info.api.dto.CreateSomuTypeDto;
import uk.gov.digital.ho.hocs.info.api.dto.SomuTypeDto;
import uk.gov.digital.ho.hocs.info.domain.model.SomuType;

import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

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
    ResponseEntity<SomuTypeDto> getSomuTypeForCaseTypeAndType(@PathVariable String caseType,
                                                              @PathVariable String type) {
        SomuType somuType = somuTypeService.getSomuTypeForCaseTypeAndType(caseType, type);
        return ResponseEntity.ok(SomuTypeDto.from(somuType));
    }

    @GetMapping(value = "/somuType/{caseType}", produces = APPLICATION_JSON_VALUE)
    ResponseEntity<Set<SomuTypeDto>> getAllSomuTypesForCaseType(@PathVariable String caseType) {
        Set<SomuType> somuTypes = somuTypeService.getAllSomuTypesForCaseType(caseType);
        return ResponseEntity.ok(somuTypes.stream().map(SomuTypeDto::from).collect(Collectors.toSet()));
    }

    @PostMapping(value = "/somuType", produces = APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity<SomuTypeDto> upsertSomuTypeForCaseTypeAndType(@RequestBody CreateSomuTypeDto somuTypeDto) {
        SomuType somuType = somuTypeService.upsertSomuTypeForCaseTypeAndType(somuTypeDto.getCaseType(),
            somuTypeDto.getType(), somuTypeDto.getSchema(), somuTypeDto.getActive());
        return ResponseEntity.ok(SomuTypeDto.from(somuType));
    }

}
