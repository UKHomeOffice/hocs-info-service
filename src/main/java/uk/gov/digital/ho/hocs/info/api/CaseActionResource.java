package uk.gov.digital.ho.hocs.info.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.digital.ho.hocs.info.api.dto.CaseTypeActionDto;

import java.util.UUID;

@RestController
public class CaseActionResource {

    private final CaseTypeService caseTypeService;

    @Autowired
    public CaseActionResource(CaseTypeService caseTypeService) {
        this.caseTypeService = caseTypeService;
    }

    @GetMapping("/actions/{actionId}/label")
    public ResponseEntity<String> getCaseActionLabelById(@PathVariable UUID actionId) {
        CaseTypeActionDto actionDto = caseTypeService.getCaseTypeActionById(actionId);
        return ResponseEntity.ok(actionDto.getActionLabel());
    }

    @GetMapping("/actions/{actionId}")
    public ResponseEntity<CaseTypeActionDto> getCaseActionById(@PathVariable UUID actionId) {
        CaseTypeActionDto actionDto = caseTypeService.getCaseTypeActionById(actionId);
        return ResponseEntity.ok(actionDto);
    }

}
