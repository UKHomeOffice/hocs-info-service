package uk.gov.digital.ho.hocs.info.team;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uk.gov.digital.ho.hocs.info.dto.GetTeamResponse;
import uk.gov.digital.ho.hocs.info.entities.Member;
import uk.gov.digital.ho.hocs.info.entities.Team;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TeamResourceTest {

    @Mock
    private TeamService teamService;
    private TeamResource teamResource;

    @Before
    public void setUp() {
        teamResource = new TeamResource(teamService);
    }

    @Test
    public void shouldReturnTeamForMember() {
//        Member theMember = new Member(99, "Robert Walpole");
//        Team theTeam = new Team(100, "Team 100", new HashSet<>(Arrays.asList(theMember)));
//
//        when(teamService.getTeamForMember(99)).thenReturn(theTeam);
//
//        ResponseEntity<GetTeamResponse> response = teamResource.getTeamFromMemberId(99);
//
//        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FOUND);
//        assertThat(response.getHeaders().getLocation().toString()).isEqualTo("/team/100");
    }

    @Test
    public void shouldReturnUnimplementedOnFullListing() {
        String[] roles = new String[] {"foo"};
        assertThat(teamResource.getAllTeams(roles).getStatusCode()).isEqualTo(HttpStatus.NOT_IMPLEMENTED);
    }

//    @Test
//    public void shouldGetTeamFromId() {
//
//        Member theMember = new Member(99, "Robert Walpole");
//        Team theTeam = new Team(101, "Team 101", new HashSet<>(Arrays.asList(theMember)));
//
//        when(teamService.getTeamFromId(101)).thenReturn(Optional.of(theTeam));
//
//        ResponseEntity<GetTeamResponse> response = teamResource.getTeamFromId(101);
//        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
//        assertThat(response.getBody().getTeam()).isEqualTo(theTeam);
//    }
//
//    @Test
//    public void shouldGetTopicFromTeamId() {
//        Member theMember = new Member(99, "Robert Walpole");
//        Team theTeam = new Team(101, "Team 101", new HashSet<>(Arrays.asList(theMember)));
//        when(teamService.getTeamForTopic(101)).thenReturn(theTeam);
//
//        ResponseEntity<GetTeamResponse> response = teamResource.getTeamFromTopicId(101);
//
//        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FOUND);
//        assertThat(response.getHeaders().getLocation().toString()).isEqualTo("/team/101");
//    }
//
//    @Test
//    public void shouldErrorOnNoTeamFromId() {
//        when(teamService.getTeamFromId(102)).thenReturn(Optional.empty());
//
//        ResponseEntity<GetTeamResponse> response = teamResource.getTeamFromId(102);
//        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
//    }
//
//    @Test
//    public void shouldErrorOnNoTeamFromMemberId() {
//        when(teamService.getTeamForMember(999)).thenThrow(new EntityNotFoundException("whoops"));
//
//        ResponseEntity response = teamResource.getTeamFromMemberId(999);
//        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
//    }
//
//    // teamService.getTeamForTopic(topicId);
//
//    @Test
//    public void shouldErrorOnNoTeamForTopic() {
//        when(teamService.getTeamForTopic(999)).thenThrow(new EntityNotFoundException("whoops"));
//
//        ResponseEntity response = teamResource.getTeamFromTopicId(999);
//        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
//    }
}
