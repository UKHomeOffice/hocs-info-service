package uk.gov.digital.ho.hocs.info.team;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.gov.digital.ho.hocs.info.dto.TeamDto;


import java.util.Set;
import java.util.UUID;

@RestController
public class TeamResource {
    private TeamService teamService;

    public TeamResource(TeamService teamService) {
        this.teamService = teamService;
    }

    @PostMapping(value = "/users/{userUUID}/teams/{teamUUID}")
    public ResponseEntity addUserToGroup(@PathVariable String userUUID, @PathVariable String teamUUID) {
        teamService.addUserToTeam(UUID.fromString(userUUID), UUID.fromString(teamUUID));
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/unit/{unitUUID}/teams" )
    public ResponseEntity createTeam(@PathVariable String unitUUID, @RequestBody TeamDto team) {
        teamService.createTeam(team, UUID.fromString(unitUUID));
        return ResponseEntity.ok().build();
    }

    @PutMapping(value = "/unit/{unitUUID}/teams/{teamUUID}")
    public ResponseEntity addTeamToUnit(@PathVariable String unitUUID, @PathVariable String teamUUID) {
        teamService.moveToNewUnit(UUID.fromString(unitUUID), UUID.fromString(teamUUID));
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
}


