package uk.gov.digital.ho.hocs.info.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.gov.digital.ho.hocs.info.api.dto.EntityDto;
import uk.gov.digital.ho.hocs.info.api.dto.GetCaseSummaryFieldsResponse;
import uk.gov.digital.ho.hocs.info.domain.model.Entity;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

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

    @GetMapping(value = "/entity/list/{name}", produces = APPLICATION_JSON_UTF8_VALUE)
    @Cacheable(value = "getEntitiesForListName", unless = "#result == null || " +
            "#name == 'MPAM_CAMPAIGNS' || #name == 'EXGRATIA_BUS_REPS' || #name == 'MPAM_BUS_UNITS_1' || " +
            "#name == 'MPAM_BUS_UNITS_2' || #name == 'MPAM_BUS_UNITS_3' || #name == 'MPAM_BUS_UNITS_4' || " +
            "#name == 'MPAM_BUS_UNITS_5' || #name == 'MPAM_BUS_UNITS_6' || #name == 'MPAM_BUS_UNITS_7' || " +
            "#name == 'MPAM_ENQUIRY_REASONS_PER' || #name == 'MPAM_ENQUIRY_REASONS_GUI' || #name == 'MPAM_ENQUIRY_REASONS_DOC' || " +
            "#name == 'MPAM_ENQUIRY_REASONS_TECH' || #name == 'MPAM_ENQUIRY_REASONS_DET' || #name == 'MPAM_ENQUIRY_REASONS_HMPO' ||" +
            "#name == 'MPAM_ENQUIRY_REASONS_OTHER' || #name == 'FOI_ACCOUNT_MANAGERS' || #name == 'FOI_INTERESTED_PARTIES' ||" +
            "#name == 'MPAM_ENQUIRY_REASONS_ALL'", key = "#name")
    public ResponseEntity<List<EntityDto>> getEntitiesForListName(@PathVariable String name) {
        List<Entity> entities = entityService.getByEntityListName(name);
        return ResponseEntity.ok(entities.stream().map(EntityDto::from).collect(Collectors.toList()));
    }

    @GetMapping(value = "/entity/{uuid}", produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<EntityDto> getEntity(@PathVariable String uuid) {
        Entity entity = entityService.getEntity(uuid);
        return ResponseEntity.ok(EntityDto.from(entity));
    }

    @GetMapping(value = "/entity/simpleName/{simpleName}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<EntityDto> getEntityBySimpleName(@PathVariable String simpleName) throws Exception {
        Entity entity = entityService.getEntityBySimpleName(simpleName);
        return ResponseEntity.ok(EntityDto.from(entity));
    }

    @PostMapping(value = "/entity/list/{listName}", produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<String> createEntity(@PathVariable String listName, @RequestBody EntityDto entityDto) {
        entityService.createEntity(listName, entityDto);
        return ResponseEntity.ok().build();
    }

    @PutMapping(value = "/entity/list/{listName}", produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<String> updateEntity(@PathVariable String listName, @RequestBody EntityDto entityDto) {
        entityService.updateEntity(listName, entityDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value="/entity/list/{listName}")
    public ResponseEntity deleteEntity(@PathVariable String listName, @RequestBody String entityUUID){
        entityService.deleteEntity(listName, entityUUID);
        return ResponseEntity.ok().build();
    }

}
