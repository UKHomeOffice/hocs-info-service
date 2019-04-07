package uk.gov.digital.ho.hocs.info.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.gov.digital.ho.hocs.info.api.dto.CaseTypeDto;
import uk.gov.digital.ho.hocs.info.api.dto.CreateCaseTypeDto;

import java.time.LocalDate;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import uk.gov.digital.ho.hocs.info.domain.model.CaseType;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
public class CaseTypeResource {

    private final CaseTypeService caseTypeService;

    @Autowired
    public CaseTypeResource(CaseTypeService caseTypeService) {
        this.caseTypeService = caseTypeService;
    }

    @GetMapping(value = "/caseType", produces = APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity<Set<CaseTypeDto>> getAllCaseTypes() {
        Set<CaseType> caseTypes = caseTypeService.getAllCaseTypes();
        return ResponseEntity.ok(caseTypes.stream().map(CaseTypeDto::from).collect(Collectors.toSet()));
    }

    @GetMapping(value = "/caseType", params = {"bulkOnly"}, produces = APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity<Set<CaseTypeDto>> getCaseTypes(@RequestParam("bulkOnly") boolean bulkOnly) {
        Set<CaseType> caseTypes = caseTypeService.getAllCaseTypesForUser(bulkOnly);
        return ResponseEntity.ok(caseTypes.stream().map(CaseTypeDto::from).collect(Collectors.toSet()));
    }

    @GetMapping(value = "/caseType/type/{type}", produces = APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity<CaseTypeDto> getCaseType(@PathVariable String type) {
        CaseType caseType = caseTypeService.getCaseType(type);
        return ResponseEntity.ok(CaseTypeDto.from(caseType));
    }

    @GetMapping(value = "/caseType/shortCode/{shortCode}", produces = APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity<CaseTypeDto> getCaseTypeByShortCode(@PathVariable String shortCode) {
        CaseType caseType = caseTypeService.getCaseTypeByShortCode(shortCode);
        return ResponseEntity.ok(CaseTypeDto.from(caseType));
    }

    @GetMapping(value = "/caseType/{caseType}/deadline", params = {"received"}, produces = APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity<LocalDate> getCaseDeadline(@PathVariable String caseType, @RequestParam String received) {
        LocalDate receivedDate = LocalDate.parse(received);
        LocalDate deadline = caseTypeService.getDeadlineForCaseType(caseType,receivedDate);
        return ResponseEntity.ok(deadline);
    }

    @GetMapping(value = "/caseType/{caseType}/stageType/deadline", params = {"received"}, produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Map<String, LocalDate>> getDeadlines(@PathVariable String caseType, @RequestParam String received) {
        LocalDate receivedDate = LocalDate.parse(received);
        Map<String, LocalDate> deadlines = caseTypeService.getAllStageDeadlinesForCaseType(caseType, receivedDate);
        return ResponseEntity.ok(deadlines);
    }

    @PostMapping
    public ResponseEntity createCaseType(CreateCaseTypeDto caseType) {
        caseTypeService.createCaseType(caseType);
        return ResponseEntity.ok().build();
    }
}
