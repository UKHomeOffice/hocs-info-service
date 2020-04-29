package uk.gov.digital.ho.hocs.info.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.gov.digital.ho.hocs.info.api.dto.CreateCorrespondentTypeDto;
import uk.gov.digital.ho.hocs.info.api.dto.CreateCorrespondentTypeResponse;
import uk.gov.digital.ho.hocs.info.api.dto.GetCorrespondentTypeResponse;
import uk.gov.digital.ho.hocs.info.domain.model.CorrespondentType;

import java.util.Set;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
public class CorrespondentTypeResource {

    private final CorrespondentTypeService correspondentTypeService;

    @Autowired
    public CorrespondentTypeResource(CorrespondentTypeService correspondentTypeService) {
        this.correspondentTypeService = correspondentTypeService;
    }

    @GetMapping(value = "/correspondentType", produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<GetCorrespondentTypeResponse> getCorrespondentTypes() {
        Set<CorrespondentType> correspondentTypes = correspondentTypeService.getAllCorrespondentTypes();
        GetCorrespondentTypeResponse response = GetCorrespondentTypeResponse.from(correspondentTypes);
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/correspondentType/{caseType}", produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<GetCorrespondentTypeResponse> getCorrespondentTypesByCaseType(@PathVariable String caseType) {
        Set<CorrespondentType> correspondentTypes = correspondentTypeService.getCorrespondentTypesByCaseType(caseType);
        GetCorrespondentTypeResponse response = GetCorrespondentTypeResponse.from(correspondentTypes);
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/correspondentType", produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<CreateCorrespondentTypeResponse> createCorrespondentType(@RequestBody CreateCorrespondentTypeDto createCorrespondentTypeDto) {
        CorrespondentType correspondentType = correspondentTypeService.createCorrespondentType(createCorrespondentTypeDto.getDisplayName(), createCorrespondentTypeDto.getType());
        return ResponseEntity.ok(CreateCorrespondentTypeResponse.from(correspondentType));
    }

}
