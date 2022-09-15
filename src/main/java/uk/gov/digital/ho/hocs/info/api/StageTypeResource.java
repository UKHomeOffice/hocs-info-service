package uk.gov.digital.ho.hocs.info.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.digital.ho.hocs.info.api.dto.StageTypeDto;
import uk.gov.digital.ho.hocs.info.api.dto.TeamDto;
import uk.gov.digital.ho.hocs.info.domain.exception.ApplicationExceptions;
import uk.gov.digital.ho.hocs.info.domain.model.StageTypeEntity;
import uk.gov.digital.ho.hocs.info.domain.model.Team;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
public class StageTypeResource {

    private final StageTypeService stageTypeService;

    private final CaseTypeService caseTypeService;

    @Autowired
    public StageTypeResource(StageTypeService stageTypeService, CaseTypeService caseTypeService) {
        this.stageTypeService = stageTypeService;
        this.caseTypeService = caseTypeService;
    }

    @GetMapping(value = "/stageType", produces = APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity<Set<StageTypeDto>> getAllStageTypes() {
        Set<StageTypeEntity> stageTypes = stageTypeService.getAllStageTypes();
        return ResponseEntity.ok(stageTypes.stream().map(StageTypeDto::from).collect(Collectors.toSet()));
    }

    /**
     * Endpoint for retrieving the name for a Stage Type by using a given Stage Type UUID
     *
     * @param stageTypeUUID the UUID of the stage type name that should be retrieved
     *
     * @return a Stage Type entity corresponding to the given UUID
     */
    @GetMapping(value = "/stageType/{stageTypeUUID}", produces = APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity<StageTypeDto> getStageTypeByUuid(String stageTypeUUID) {
        Set<StageTypeEntity> stageTypes = stageTypeService.getAllStageTypes();
        for (StageTypeEntity stageType : stageTypes) {
            if (stageType.getUuid().equals(UUID.fromString(stageTypeUUID))) {
                return ResponseEntity.ok(StageTypeDto.from(stageType));
            }
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping(value = "/stages/caseType/{caseType}", produces = APPLICATION_JSON_VALUE)
    ResponseEntity<Set<StageTypeDto>> getStagesForCaseType(@PathVariable String caseType) {
        final UUID caseTypeUuid = caseTypeService.getCaseType(caseType).getUuid();

        final Set<StageTypeDto> stages = stageTypeService.getAllStageTypesByCaseType(caseTypeUuid).stream().map(
            StageTypeDto::from).collect(Collectors.toSet());

        return ResponseEntity.ok(stages);
    }

    @GetMapping(value = "/stageType/{stageType}/team", produces = APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity<TeamDto> getTeamForStageType(@PathVariable String stageType) {
        Team team = stageTypeService.getTeamForStageType(stageType);
        return ResponseEntity.ok(TeamDto.fromWithoutPermissions(team));
    }

    @GetMapping(value = "/stageType/{stageType}/contributions", produces = APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity<Boolean> getCanDisplayContributionsByType(@PathVariable String stageType) {
        try {
            Boolean canDisplayContributions = stageTypeService.getCanDisplayContributionsForStageType(stageType);
            return ResponseEntity.ok(canDisplayContributions);
        } catch (ApplicationExceptions.EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

}
