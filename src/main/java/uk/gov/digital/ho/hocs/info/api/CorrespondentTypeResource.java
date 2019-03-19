package uk.gov.digital.ho.hocs.info.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.Set;

import uk.gov.digital.ho.hocs.info.api.dto.CreateCorrespondentTypeDto;
import uk.gov.digital.ho.hocs.info.api.dto.CreateCorrespondentTypeResponse;
import uk.gov.digital.ho.hocs.info.api.dto.GetCorrespondentTypesResponse;
import uk.gov.digital.ho.hocs.info.domain.model.CorrespondentType;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
public class CorrespondentTypeResource {

    private final CorrespondentTypeService correspondentTypeService;

    @Autowired
    public CorrespondentTypeResource(CorrespondentTypeService correspondentTypeService) {
        this.correspondentTypeService = correspondentTypeService;
    }

    @GetMapping(value = "/correspondentType", produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<GetCorrespondentTypesResponse> getCorrespondentTypes() {
        Set<CorrespondentType> correspondentTypes = correspondentTypeService.getAllCorrespondentTypes();
        return ResponseEntity.ok(GetCorrespondentTypesResponse.from(correspondentTypes));
    }

    @PostMapping(value = "/correspondentType/", produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<CreateCorrespondentTypeResponse> createCorrespondentType(@RequestBody CreateCorrespondentTypeDto createCorrespondentTypeDto ) {
        CorrespondentType correspondentType = correspondentTypeService.createCorrespondentType(createCorrespondentTypeDto.getDisplayName(), createCorrespondentTypeDto.getType());
        return ResponseEntity.ok(CreateCorrespondentTypeResponse.from(correspondentType));

    }

}
