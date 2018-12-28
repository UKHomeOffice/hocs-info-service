package uk.gov.digital.ho.hocs.info.api.team;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.gov.digital.ho.hocs.info.api.dto.TeamDto;
import uk.gov.digital.ho.hocs.info.api.dto.UpdateTeamNameRequest;
import uk.gov.digital.ho.hocs.info.api.dto.UpdateTeamPermissionsRequest;


import java.util.Set;
import java.util.UUID;

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
        TeamDto createdTeam = teamService.createTeam(team, UUID.fromString(unitUUID));
        return ResponseEntity.ok(createdTeam);
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
        return ResponseEntity.ok(teamService.getTeamsForUnit(UUID.fromString(unitUUID)));
    }

    @GetMapping(value = "/unit/{unitUUID}/teams/{teamUUID}")
    public ResponseEntity<TeamDto> getTeam(@PathVariable String unitUUID, @PathVariable String teamUUID) {
        return ResponseEntity.ok(teamService.getTeam(UUID.fromString(teamUUID)));
    }

    @GetMapping(value = "/team")
    public ResponseEntity<Set<TeamDto>> getActiveTeams() {
        return ResponseEntity.ok(teamService.getAllActiveTeams());
    }
}


