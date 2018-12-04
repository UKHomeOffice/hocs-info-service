package uk.gov.digital.ho.hocs.info.stagetype;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.digital.ho.hocs.info.dto.StageTypeDto;
import uk.gov.digital.ho.hocs.info.dto.GetStageTypesResponse;
import uk.gov.digital.ho.hocs.info.entities.StageTypeEntity;

import java.util.Set;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
public class StageTypeResource {

    private final StageTypeService stageTypeService;

    @Autowired
    public StageTypeResource(StageTypeService stageTypeService) {
        this.stageTypeService = stageTypeService;
    }

    @GetMapping(value = "/stageType", produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<GetStageTypesResponse> getStageTypes() {
        Set<StageTypeEntity> stageTypes = stageTypeService.getAllStageTypes();
        return ResponseEntity.ok(GetStageTypesResponse.from(stageTypes));
    }

}
