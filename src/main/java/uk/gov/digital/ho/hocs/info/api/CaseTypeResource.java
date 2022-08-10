package uk.gov.digital.ho.hocs.info.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.gov.digital.ho.hocs.info.api.dto.CaseConfigDto;
import uk.gov.digital.ho.hocs.info.api.dto.CaseTypeActionDto;
import uk.gov.digital.ho.hocs.info.api.dto.CaseTypeDto;
import uk.gov.digital.ho.hocs.info.api.dto.CreateCaseTypeDto;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import uk.gov.digital.ho.hocs.info.domain.model.CaseConfig;
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
    ResponseEntity<Set<CaseTypeDto>> getAllCaseTypes(@RequestParam(required = false, name = "addCasesWithPreviousType", defaultValue = "true") Boolean addCasesWithPreviousType) {
        Set<CaseType> caseTypes = caseTypeService.getAllCaseTypes(addCasesWithPreviousType);
        return ResponseEntity.ok(caseTypes.stream().map(CaseTypeDto::from).collect(Collectors.toSet()));
    }

    @GetMapping(value = "/caseType", params = {"bulkOnly"}, produces = APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity<Set<CaseTypeDto>> getCaseTypes(
            @RequestParam("bulkOnly") boolean bulkOnly,
            @RequestParam(required = false, name = "initialCaseType", defaultValue = "true") Boolean initialCaseType) {
        Set<CaseType> caseTypes = caseTypeService.getAllCaseTypesForUser(bulkOnly, initialCaseType);
        return ResponseEntity.ok(caseTypes.stream().map(CaseTypeDto::from).collect(Collectors.toSet()));
    }

    @GetMapping(value = "/caseType/type/{type}", produces = APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity<CaseTypeDto> getCaseType(@PathVariable String type) {
        CaseType caseType = caseTypeService.getCaseType(type);
        return ResponseEntity.ok(CaseTypeDto.from(caseType));
    }

    @Deprecated(forRemoval = true)
    @GetMapping(value = "/caseType/{type}/config", produces = APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity<CaseConfigDto> getCaseConfig(@PathVariable String type) {
        CaseConfig caseConfig = caseTypeService.getCaseConfig(type);
        return ResponseEntity.ok(CaseConfigDto.from(caseConfig));
    }

    @GetMapping(value = "/caseType/shortCode/{shortCode}", produces = APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity<CaseTypeDto> getCaseTypeByShortCode(@PathVariable String shortCode) {
        CaseType caseType = caseTypeService.getCaseTypeByShortCode(shortCode);
        return ResponseEntity.ok(CaseTypeDto.from(caseType));
    }

    @GetMapping(value = "/caseType/{caseType}/documentTags", produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<String>> getDocumentTags(@PathVariable String caseType) {
        List<String> documentTags = caseTypeService.getDocumentTagsForCaseType(caseType);
        return ResponseEntity.ok(documentTags);
    }

    public ResponseEntity<Void> createCaseType(CreateCaseTypeDto caseType) {
        caseTypeService.createCaseType(caseType);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/caseType/actions")
    public ResponseEntity<List<CaseTypeActionDto>> getAllCaseTypeActions() {
        List<CaseTypeActionDto> caseActions = caseTypeService.getAllCaseActions();
        return ResponseEntity.ok(caseActions);
    }

    @GetMapping("/caseType/{caseType}/actions")
    public ResponseEntity<List<CaseTypeActionDto>> getCaseActionsByType(@PathVariable String caseType) {
        List<CaseTypeActionDto> caseActions = caseTypeService.getCaseActionsByCaseType(caseType);
        return ResponseEntity.ok(caseActions);
    }

    @GetMapping("/caseType/{caseType}/exemptionDates")
    public ResponseEntity<Set<LocalDate>> getExemptionDatesByType(@PathVariable String caseType) {
        Set<LocalDate> caseActions = caseTypeService.getExemptionDatesByCaseType(caseType);
        return ResponseEntity.ok(caseActions);
    }

    @Deprecated // the only resource accessed is the case_type_action, so use "/actions/{actionId}"
    @GetMapping("/caseType/{caseType}/actions/{actionId}")
    public ResponseEntity<CaseTypeActionDto> getCaseActionById(@PathVariable UUID actionId) {
        CaseTypeActionDto actionDto = caseTypeService.getCaseTypeActionById(actionId);
        return ResponseEntity.ok(actionDto);
    }
}
