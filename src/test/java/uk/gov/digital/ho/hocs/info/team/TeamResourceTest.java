package uk.gov.digital.ho.hocs.info.team;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import uk.gov.digital.ho.hocs.info.dto.GetTeamResponse;
import uk.gov.digital.ho.hocs.info.entities.Member;
import uk.gov.digital.ho.hocs.info.entities.Team;

import java.net.URI;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

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
        Member theMember = new Member(99, "Robert Walpole");
        Team theTeam = new Team(100, "Team 100", new HashSet<>(Arrays.asList(theMember)));

        when(teamService.getTeamForMember(99)).thenReturn(theTeam);

        ResponseEntity<GetTeamResponse> response = teamResource.getTeamFromMemberId(99);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FOUND);
        assertThat(response.getHeaders().getLocation().toString()).isEqualTo("/team/100");
    }
}
