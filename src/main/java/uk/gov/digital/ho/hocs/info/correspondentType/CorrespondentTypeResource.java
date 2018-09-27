package uk.gov.digital.ho.hocs.info.correspondentType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.digital.ho.hocs.info.dto.GetCorrespondentTypeResponse;
import uk.gov.digital.ho.hocs.info.entities.CorrespondentType;

import java.util.Set;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
public class CorrespondentTypeResource {

    private final CorrespondentTypeService correspondentTypeService;

    @Autowired
    public CorrespondentTypeResource(CorrespondentTypeService correspondentTypeService) {
        this.correspondentTypeService = correspondentTypeService;
    }

    @GetMapping(value = "/correspondenttype", produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<GetCorrespondentTypeResponse> getCorrespondentTypes() {
        Set<CorrespondentType> correspondentTypes = correspondentTypeService.getCorrespondentTypes();
        return ResponseEntity.ok(GetCorrespondentTypeResponse.from(correspondentTypes));
    }

}
