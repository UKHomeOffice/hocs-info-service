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

    @GetMapping(value = "/stageType/{stageType}/team", produces = APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity<TeamDto> getTeamForStageType(@PathVariable String stageType) {
        Team team = stageTypeService.getTeamForStageType(stageType);
        return ResponseEntity.ok(TeamDto.fromWithoutPermissions(team));
    }

    @GetMapping(value = "/stageType/{stageType}/deadline", params = {"received","caseDeadline"}, produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<LocalDate> getDeadlineByStage(@PathVariable String stageType, @RequestParam String received, @RequestParam String caseDeadline) {
        try {
            LocalDate receivedDate = LocalDate.parse(received);
            LocalDate caseDeadlineDate = LocalDate.parse(caseDeadline);
            LocalDate deadline = stageTypeService.getDeadlineForStageType(stageType, receivedDate, caseDeadlineDate);
            return ResponseEntity.ok(deadline);
        } catch (ApplicationExceptions.EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

}
