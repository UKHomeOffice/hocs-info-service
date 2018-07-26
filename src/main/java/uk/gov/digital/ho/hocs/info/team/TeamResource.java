package uk.gov.digital.ho.hocs.info.team;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.gov.digital.ho.hocs.info.dto.GetMembersResponse;
import uk.gov.digital.ho.hocs.info.dto.GetTeamResponse;
import uk.gov.digital.ho.hocs.info.entities.Member;
import uk.gov.digital.ho.hocs.info.entities.Team;
import uk.gov.digital.ho.hocs.info.exception.EntityNotFoundException;
import uk.gov.digital.ho.hocs.info.member.MemberService;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@Slf4j
@RestController
public class TeamResource {

    private final TeamService teamService;

    @Autowired
    public TeamResource(final TeamService teamService) {
        this.teamService = teamService;
    }

    @RequestMapping(value = "/teams", method = RequestMethod.GET, produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<GetMembersResponse> getAllTeams(@RequestHeader("X-Auth-Roles") String[] roles) {
        return ResponseEntity.status(501).build();
    }

    @RequestMapping(value = "/team/{id}", method = RequestMethod.GET, produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<GetTeamResponse> getTeamFromId(@PathVariable int id) {
            Optional<Team> team = teamService.getTeamFromId(id);
            if (team.isPresent()) {
                return ResponseEntity.ok(new GetTeamResponse(team.get()));
            }
            return ResponseEntity.notFound().build();
    }

    @RequestMapping(value = "/member/{memberId}/team", method = RequestMethod.GET, produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<GetTeamResponse> getTeamFromMemberId(@PathVariable int memberId) {
        try {
            Team team = teamService.getTeamForMember(memberId);

            String location = "/team/".concat(String.valueOf(team.getId()));
            return ResponseEntity.status(HttpStatus.FOUND_302).header("Location", location).build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
