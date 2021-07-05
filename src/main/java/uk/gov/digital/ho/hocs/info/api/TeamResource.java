package uk.gov.digital.ho.hocs.info.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import uk.gov.digital.ho.hocs.info.api.dto.*;
import uk.gov.digital.ho.hocs.info.domain.exception.ApplicationExceptions;
import uk.gov.digital.ho.hocs.info.domain.model.Team;
import uk.gov.digital.ho.hocs.info.security.Base64UUID;

import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;
import static net.logstash.logback.argument.StructuredArguments.value;
import static uk.gov.digital.ho.hocs.info.application.LogEvent.*;

@RestController
@Slf4j
public class TeamResource {
    private TeamService teamService;

    public TeamResource(TeamService teamService) {
        this.teamService = teamService;
    }

    @PostMapping(value = "/users/team/{teamUUID}")
    public ResponseEntity addUserToGroup(@PathVariable String teamUUID, @RequestBody List<String> userUUIDs) {
        List<UUID> convertedUserUuids = userUUIDs.stream().map(UUID::fromString).collect(Collectors.toList());
        teamService.addUsersToTeam(convertedUserUuids, UUID.fromString(teamUUID));
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/unit/{unitUUID}/teams")
    public ResponseEntity<TeamDto> createUpdateTeam(@PathVariable String unitUUID, @RequestBody TeamDto team) {
        try {
            Team createdTeam = teamService.createTeam(team, UUID.fromString(unitUUID));
            return ResponseEntity.ok(TeamDto.from(createdTeam));
        } catch (ApplicationExceptions.EntityAlreadyExistsException entityAlreadyExistsException) {
            log.error(entityAlreadyExistsException.getMessage(), value(EVENT, FAILED_TO_CREATE_TEAM));
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @PatchMapping(value = "/team/{teamUuid}")
    @Transactional
    public ResponseEntity patchTeam(@PathVariable UUID teamUuid, @RequestBody PatchTeamDto teamPatch) {
        try {
            teamService.patchTeam(teamUuid, teamPatch);

            return ResponseEntity.ok().build();
        } catch (ApplicationExceptions.EntityAlreadyExistsException entityAlreadyExistsException) {
            log.error(entityAlreadyExistsException.getMessage(), value(EVENT, FAILED_TO_PATCH_TEAM));
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (Exception e) {
            log.error(e.getMessage(), value(EVENT, FAILED_TO_PATCH_TEAM));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping(value = "/unit/{unitUUID}/teams/{teamUUID}")
    public ResponseEntity addTeamToUnit(@PathVariable String unitUUID, @PathVariable String teamUUID) {
        teamService.moveToNewUnit(UUID.fromString(unitUUID), UUID.fromString(teamUUID));
        return ResponseEntity.ok().build();
    }

    @PutMapping(value = "/team/{teamUUID}")
    public ResponseEntity updateTeamName(@PathVariable String teamUUID, @RequestBody UpdateTeamNameRequest team) {
        teamService.updateTeamName(UUID.fromString(teamUUID), team.getDisplayName());
        return ResponseEntity.ok().build();
    }

    @PutMapping(value = "/team/{teamUUID}/lettername")
    public ResponseEntity updateTeamLetterName(@PathVariable String teamUUID, @RequestBody UpdateTeamLetterNameRequest team) {
        teamService.updateTeamLetterName(UUID.fromString(teamUUID), team.getLetterName());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "/team/{teamUUID}")
    public ResponseEntity deleteTeam(@PathVariable String teamUUID) {
        teamService.deleteTeam(UUID.fromString(teamUUID));
        return ResponseEntity.ok().build();
    }

    @PutMapping(value = "/team/{teamUUID}/permissions")
    public ResponseEntity updateTeamPermissions(@PathVariable String teamUUID, @RequestBody UpdateTeamPermissionsRequest team) {
        teamService.updateTeamPermissions(UUID.fromString(teamUUID), team.getPermissions());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "/team/{teamUUID}/permissions")
    public ResponseEntity deleteTeamPermissions(@PathVariable String teamUUID, @RequestBody UpdateTeamPermissionsRequest team) {
        teamService.deleteTeamPermissions(UUID.fromString(teamUUID), team.getPermissions());
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/unit/{unitUUID}/teams")
    public ResponseEntity<Set<TeamDto>> getTeamsForUnit(@PathVariable String unitUUID) {
        Set<Team> teams = teamService.getTeamsForUnit(UUID.fromString(unitUUID));
        return ResponseEntity.ok(teams.stream().map(TeamDto::from).collect(Collectors.toSet()));
    }

    @GetMapping(value = "/user/{userUUID}/teams")
    public ResponseEntity<Set<TeamDto>> getTeamsForUser(@PathVariable String userUUID) {
        Set<Team> teams = teamService.getTeamsForUser(UUID.fromString(userUUID));
        return ResponseEntity.ok(teams.stream().map(TeamDto::from).collect(Collectors.toSet()));
    }

    @GetMapping(value = "/unit/{unitUUID}/teams/{teamUUID}")
    public ResponseEntity<TeamDto> getTeam(@PathVariable String unitUUID, @PathVariable String teamUUID) {
        Team team = teamService.getTeam(UUID.fromString(teamUUID));
        return ResponseEntity.ok(TeamDto.from(team));
    }

    @GetMapping(value = "/team/{teamUUID}", produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<TeamDto> getTeam(@PathVariable UUID teamUUID) {
        Team team = teamService.getTeam(teamUUID);
        return ResponseEntity.ok(TeamDto.from(team));
    }

    @GetMapping(value = "/team/{teamUUID}/code", produces = TEXT_PLAIN_VALUE)
    public ResponseEntity<String> getTeamCode(@PathVariable UUID teamUUID) {
        return ResponseEntity.ok(Base64UUID.uuidToBase64String(teamUUID));
    }

    @GetMapping(value = "/team/{teamUUID}/unit", produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<UnitDto> getUnitByTeam(@PathVariable UUID teamUUID) {
        Team team = teamService.getTeam(teamUUID);
        return ResponseEntity.ok(UnitDto.from(team.getUnit()));
    }

    @GetMapping(value = "/team/{teamUUID}/move_options", produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Set<TeamDto>> getMoveToAnotherTeamOptions(@PathVariable UUID teamUUID) {
        Team team = teamService.getTeam(teamUUID);

        Set<Team> teams = team.getUnit().isAllowBulkTeamTransfer() ?
                teamService.findActiveTeamsByUnitUuid(team.getUnit().getUuid()) : Collections.emptySet();

        return ResponseEntity.ok(teams.stream().map(TeamDto::from).collect(Collectors.toSet()));
    }

    @GetMapping(value = "/team")
    public ResponseEntity<Set<TeamDto>> getActiveTeams() {
        Set<Team> teams = teamService.getAllActiveTeams();
        return ResponseEntity.ok(teams.stream().map(TeamDto::from).collect(Collectors.toSet()));
    }

    @GetMapping(value = "/team/all")
    public ResponseEntity<Set<TeamDto>> getAllTeams() {
        Set<Team> teams = teamService.getAllTeams();
        return ResponseEntity.ok(teams.stream().map(TeamDto::from).collect(Collectors.toSet()));
    }

    @GetMapping(value = "/teams", params = {"unit"}, produces = APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity<Set<TeamDto>> getCaseTypes(@RequestParam("unit") String unitShortCode) {
        Set<Team> teams = teamService.getAllActiveTeamsByUnitShortCode(unitShortCode);
        return ResponseEntity.ok(teams.stream().map(TeamDto::fromWithoutPermissions).collect(Collectors.toSet()));
    }

    @GetMapping(value = "/teams/descendants/stage/{stageUUID}/case/{caseUUID}", produces = APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity<Set<TeamDto>> getFirstDescendantTeamsFromCurrentTeam(@PathVariable UUID stageUUID, @PathVariable UUID caseUUID) { ;
        Set<Team> firstDescendantTeams = teamService.getAllFirstDescendantTeamsFromCurrentTeam(caseUUID, stageUUID);
        return ResponseEntity.ok(firstDescendantTeams.stream().map(TeamDto::fromWithoutPermissions).collect(Collectors.toSet()));
    }

    @GetMapping(value = "/teams/drafters")
    public ResponseEntity<Set<TeamDto>> getdraftingteams() {
        Set<Team> teams = teamService.getAllActiveTeams();
        return ResponseEntity.ok(teams.stream().map(TeamDto::fromWithoutPermissions).collect(Collectors.toSet()));
    }

    @GetMapping(value = "/team/case/{caseUUID}/topic/{topicUUID}/stage/{stageType}")
    public ResponseEntity<TeamDto> getActiveTeams(@PathVariable UUID caseUUID, @PathVariable UUID topicUUID, @PathVariable String stageType) {
        Team team = teamService.getTeamByTopicAndStage(caseUUID, topicUUID, stageType);
        return ResponseEntity.ok(TeamDto.from(team));
    }

    @GetMapping(value = "/teams/topic/{topicUUID}")
    public ResponseEntity<Set<TeamDto>> getTeamsByTopic(@PathVariable UUID topicUUID) {
        Set<Team> teams = teamService.getTeamsByTopic(topicUUID);
        return ResponseEntity.ok(teams.stream().map(TeamDto::fromWithoutPermissions).collect(Collectors.toSet()));
    }

    @GetMapping(value = "/team/stage/{stageType}/text/{text}")
    public ResponseEntity<TeamDto> getActiveTeamsByLinkValue(@PathVariable String stageType, @PathVariable String text) {
        Team team = teamService.getTeamByStageAndText(stageType, text);
        return ResponseEntity.ok(TeamDto.from(team));
    }

    @GetMapping(value = "/team/stage/{stageType}")
    public ResponseEntity<Set<TeamDto>> getActiveTeamsByStageType(@PathVariable String stageType) {
        Set<Team> teams = teamService.getActiveTeamsByStageType(stageType);
        return ResponseEntity.ok(teams.stream().map(TeamDto::fromWithoutPermissions).collect(Collectors.toSet()));
    }

    @DeleteMapping(value = "/users/{userUUID}/team/{teamUUID}")
    public ResponseEntity removeUserFromTeam(@PathVariable String userUUID, @PathVariable String teamUUID) {
        teamService.removeUserFromTeam(UUID.fromString(userUUID), UUID.fromString(teamUUID));
        return ResponseEntity.ok().build();
    }
}
