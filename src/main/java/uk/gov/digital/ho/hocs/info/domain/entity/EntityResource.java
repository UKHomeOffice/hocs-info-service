package uk.gov.digital.ho.hocs.info.domain.entity;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.gov.digital.ho.hocs.info.domain.entity.dto.EntityDto;
import uk.gov.digital.ho.hocs.info.domain.entity.dto.GetCaseSummaryFieldsResponse;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

    @GetMapping(value = "/entity/list/{name}", produces = APPLICATION_JSON_UTF8_VALUE)
    @Cacheable(value = "getEntitiesForListName", unless = "#result == null || #name == 'MPAM_CAMPAIGNS'", key = "#name")
    public ResponseEntity<List<EntityDto>> getEntitiesForListName(@PathVariable String name) {
        List<Entity> entities = entityService.getByEntityListName(name);
        return ResponseEntity.ok(entities.stream().map(EntityDto::from).collect(Collectors.toList()));
    }

    @GetMapping(value = "/entity/{uuid}", produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<EntityDto> getEntity(@PathVariable String uuid) {
        Entity entity = entityService.getEntity(uuid);
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


}