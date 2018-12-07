package uk.gov.digital.ho.hocs.info.entity;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.gov.digital.ho.hocs.info.entity.dto.GetCaseSummaryFieldsResponse;

import java.util.Set;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@Slf4j
@RestController
class EntityResource {

    private final EntityService entityService;

    @Autowired
    public EntityResource(EntityService entityService) {
        this.entityService = entityService;
    }

    @GetMapping(value = "/caseType/{caseType}/summary", produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<GetCaseSummaryFieldsResponse> getCaseSummary(@PathVariable String caseType) {
        Set<Entity> entities = entityService.getBySimpleName("caseType", caseType, "summary");
        return ResponseEntity.ok(GetCaseSummaryFieldsResponse.from(entities));
    }

}