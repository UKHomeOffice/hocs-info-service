package uk.gov.digital.ho.hocs.info.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.gov.digital.ho.hocs.info.api.dto.TeamDto;
import uk.gov.digital.ho.hocs.info.api.dto.UpdateTeamLetterNameRequest;
import uk.gov.digital.ho.hocs.info.api.dto.UpdateTeamNameRequest;
import uk.gov.digital.ho.hocs.info.api.dto.UpdateTeamPermissionsRequest;
import uk.gov.digital.ho.hocs.info.domain.model.Team;


import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
public class TeamResource {
    private TeamService teamService;

    public TeamResource(TeamService teamService) {
        this.teamService = teamService;
    }

    @PostMapping(value = "/users/{userUUID}/team/{teamUUID}")
    public ResponseEntity addUserToGroup(@PathVariable String userUUID, @PathVariable String teamUUID) {
        teamService.addUserToTeam(UUID.fromString(userUUID), UUID.fromString(teamUUID));
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/unit/{unitUUID}/teams" )
    public ResponseEntity<TeamDto> createUpdateTeam(@PathVariable String unitUUID, @RequestBody TeamDto team) {
        Team createdTeam = teamService.createTeam(team, UUID.fromString(unitUUID));
        return ResponseEntity.ok(TeamDto.from(createdTeam));
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
        teamService.updateTeamPermissions(UUID.fromString(teamUUID),team.getPermissions());
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

    @GetMapping(value = "/team")
    public ResponseEntity<Set<TeamDto>> getActiveTeams() {
        Set<Team> teams = teamService.getAllActiveTeams();
        return ResponseEntity.ok(teams.stream().map(TeamDto::from).collect(Collectors.toSet()));
    }

    @GetMapping(value = "/teams", params = {"unit"}, produces = APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity<Set<TeamDto>> getCaseTypes(@RequestParam("unit") String unitShortCode) {
        Set<Team> teams = teamService.getAllActiveTeamsByUnitShortCode(unitShortCode);
        return ResponseEntity.ok(teams.stream().map(TeamDto::fromWithoutPermissions).collect(Collectors.toSet()));
    }

    @GetMapping(value = "/teams/drafters")
    public ResponseEntity<Set<TeamDto>> getdraftingteams() {
        Set<Team> teams = teamService.getAllActiveTeams();
        return ResponseEntity.ok(teams.stream().map(TeamDto::fromWithoutPermissions).collect(Collectors.toSet()));
    }

    @GetMapping(value = "/team/case/{caseUUID}/topic/{topicUUID}/stage/{stageType}")
    public ResponseEntity<TeamDto> getTeamForTopicAndStage(@PathVariable UUID caseUUID, @PathVariable UUID topicUUID, @PathVariable String stageType) {
        Team team = teamService.getTeamByTopicAndStage(caseUUID, topicUUID, stageType);
        return ResponseEntity.ok(TeamDto.from(team));
    }

    @GetMapping(value = "/team/case/{caseUUID}/region/{regionUUID}/stage/{stageType}")
    public ResponseEntity<TeamDto> getTeamForRegionAndStage(@PathVariable UUID caseUUID, @PathVariable UUID regionUUID, @PathVariable String stageType) {
        Team team = teamService.getTeamByRegionAndStage(caseUUID, regionUUID, stageType);
        return ResponseEntity.ok(TeamDto.from(team));
    }

    @GetMapping(value = "/team/case/{caseUUID}/unit/{unitUUID}/stage/{stageType}")
    public ResponseEntity<TeamDto> getTeamForUnitAndStage(@PathVariable UUID caseUUID, @PathVariable UUID unitUUID, @PathVariable String stageType) {
        //Team team = teamService.getTeamByUnitAndStage(caseUUID, unitUUID, stageType);
        //return ResponseEntity.ok(TeamDto.from(team));

        //TODO Update when MP - constituency and linking table changes have been made.
        return getTeam(UUID.fromString("21dbe3f1-c914-4ed2-8baa-831d3aa16aa4"));
    }

    @GetMapping(value = "/team/case/{caseUUID}/constituency/{constituencyUUID}/stage/{stageType}")
    public ResponseEntity<TeamDto> getTeamForConstituencyAndStage(@PathVariable UUID caseUUID, @PathVariable UUID constituencyUUID, @PathVariable String stageType) {
        //Team team = teamService.getTeamByConstituencyAndStage(caseUUID, unitUUID, stageType);
        //return ResponseEntity.ok(TeamDto.from(team));

        //TODO Update when MP - constituency and linking table changes have been made.
        return getTeam(UUID.fromString("40e6db75-239f-48ed-a998-d9097e6be8cf"));
    }
    @DeleteMapping(value = "/users/{userUUID}/team/{teamUUID}")
    public ResponseEntity removeUserFromTeam(@PathVariable String userUUID, @PathVariable String teamUUID) {
        teamService.removeUserFromTeam(UUID.fromString(userUUID), UUID.fromString(teamUUID));
        return ResponseEntity.ok().build();
    }
}


