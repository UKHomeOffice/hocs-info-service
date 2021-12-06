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
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
public class StageTypeResource {

    private final StageTypeService stageTypeService;

    @Autowired
    public StageTypeResource(StageTypeService stageTypeService) {
        this.stageTypeService = stageTypeService;
    }

    @GetMapping(value = "/stageType", produces = APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity<Set<StageTypeDto>> getAllStageTypes() {
        Set<StageTypeEntity> stageTypes = stageTypeService.getAllStageTypes();
        return ResponseEntity.ok(stageTypes.stream().map(StageTypeDto::from).collect(Collectors.toSet()));
    }

    /**
     * Endpoint for retrieving the name for a Stage Type by using a given Stage Type UUID
     * @param stageTypeUUID the UUID of the stage type name that should be retrieved
     * @return a Stage Type entity corresponding to the given UUID
     */
    @GetMapping(value = "/stageType/{stageTypeUUID}", produces = APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity<StageTypeDto> getStageTypeByUuid(String stageTypeUUID) {
        Set<StageTypeEntity> stageTypes = stageTypeService.getAllStageTypes();
        for (StageTypeEntity stageType : stageTypes){
            if (stageType.getUuid().equals(UUID.fromString(stageTypeUUID))){
                return ResponseEntity.ok(StageTypeDto.from(stageType));
            }
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping(value = "/stageType/{stageType}/team", produces = APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity<TeamDto> getTeamForStageType(@PathVariable String stageType) {
        Team team = stageTypeService.getTeamForStageType(stageType);
        return ResponseEntity.ok(TeamDto.fromWithoutPermissions(team));
    }

    @GetMapping(value = "/stageType/{stageType}/deadline", produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<LocalDate> getDeadlineByStage(@PathVariable String stageType, @RequestParam String received, @RequestParam String caseDeadline, @RequestParam(required = false, defaultValue = "false") boolean overrideSla) {
        try {
            LocalDate receivedDate = LocalDate.parse(received);
            LocalDate caseDeadlineDate = LocalDate.parse(caseDeadline);
            LocalDate deadline;
            if (!overrideSla) {
                deadline = stageTypeService.getDeadlineForStageType(stageType, receivedDate, caseDeadlineDate);
            } else {
                deadline = stageTypeService.getDeadlineForStageTypeOverrideSla(stageType, receivedDate, caseDeadlineDate);
            }
            return ResponseEntity.ok(deadline);
        } catch (ApplicationExceptions.EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping(value = "/stageType/{stageType}/deadlineWarning", produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<LocalDate> getDeadlineWarningByStage(@PathVariable String stageType, @RequestParam String received, @RequestParam String caseDeadlineWarning, @RequestParam(required = false, defaultValue = "false") boolean overrideSla) {
        try {
            LocalDate receivedDate = LocalDate.parse(received);
            LocalDate caseDeadlineWarningDate = LocalDate.parse(caseDeadlineWarning);
            LocalDate deadline;
            if (!overrideSla) {
                deadline = stageTypeService.getDeadlineWarningForStageType(stageType, receivedDate, caseDeadlineWarningDate);
            } else {
                deadline = stageTypeService.getDeadlineWarningForStageTypeOverrideSla(stageType, receivedDate, caseDeadlineWarningDate);
            }
            return ResponseEntity.ok(deadline);
        } catch (ApplicationExceptions.EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
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
