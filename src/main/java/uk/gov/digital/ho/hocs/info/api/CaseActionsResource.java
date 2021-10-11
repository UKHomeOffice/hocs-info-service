package uk.gov.digital.ho.hocs.info.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.digital.ho.hocs.info.api.dto.CaseActionTypeDto;

import java.util.List;

@RestController
@Slf4j
public class CaseActionsResource {

    private final CaseActionsTypeService caseActionTypeService;

    @Autowired
    public CaseActionsResource(CaseActionsTypeService caseActionTypeService) {
        this.caseActionTypeService = caseActionTypeService;
    }

    @GetMapping("/case-actions/{caseType}")
    public ResponseEntity<List<CaseActionTypeDto>> getCaseActionsByType(@PathVariable String caseType) {

        log.debug("Received request for case actions with caseType {}", caseType);
        List<CaseActionTypeDto> caseActions = caseActionTypeService.getCaseActionsByCaseType(caseType);
        log.info("Found {} case actions for caseType {}", caseActions.size(), caseType);

        return ResponseEntity.ok(caseActions);
    }
}
