package uk.gov.digital.ho.hocs.info.team;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.digital.ho.hocs.info.entities.Member;
import uk.gov.digital.ho.hocs.info.entities.Team;
import uk.gov.digital.ho.hocs.info.repositories.TeamRepository;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class TeamServiceTest {

    @Mock
    private TeamRepository teamRepository;
    private TeamService teamService;

    @Before
    public void setUp() {
        this.teamService = new TeamService(teamRepository);
    }

    /*
     * Get team by team:
     */

    @Test
    public void shouldReturnTeamFromId(){

        Set<Member> members = memberList();
        when(teamRepository.findById(1)).thenReturn(Optional.of(new Team(1,"Aleph", members)));

        Team team = teamService.getTeamFromId(1).get();

        assertThat(team.getId()).isEqualTo(1);
        assertThat(team.getDisplayName()).isEqualTo("Aleph");
        assertThat(team.getMembers()).isEqualTo(members);
    }

    @Test
    public void shouldReturnEmptyOptionalOnBadTeamId(){
        when(teamRepository.findById(2)).thenReturn(Optional.empty());
        assertThat(teamService.getTeamFromId(2).isPresent()).isFalse();
    }

    /*
     * Get team by topic:
     */

    @Test
    public void shouldReturnTeamFromTopic(){

        Set<Member> members = memberList();
        when(teamRepository.getTeamFromTopicId(3)).thenReturn(new Team(3,"He", members));

        Team team = teamService.getTeamForTopic(3);

        assertThat(team.getId()).isEqualTo(3);
        assertThat(team.getDisplayName()).isEqualTo("He");
        assertThat(team.getMembers()).isEqualTo(members);
    }

    @Test
    public void shouldReturnEmptyOptionalOnBadTopicId(){
        when(teamRepository.findById(4)).thenReturn(Optional.empty());
        assertThat(teamService.getTeamFromId(4).isPresent()).isFalse();
    }

    /*
     * Get team by member:
     */

    @Test
    public void shouldReturnTeamFromMember(){

        Set<Member> members = memberList();
        when(teamRepository.getTeamFromMemberId(5)).thenReturn(new Team(5,"Vav", members));

        Team team = teamService.getTeamForMember(5);

        assertThat(team.getId()).isEqualTo(5);
        assertThat(team.getDisplayName()).isEqualTo("Vav");
        assertThat(team.getMembers()).isEqualTo(members);
    }

    @Test
    public void shouldReturnEmptyOptionalOnBadMemberId(){
        when(teamRepository.findById(6)).thenReturn(Optional.empty());
        assertThat(teamService.getTeamFromId(6).isPresent()).isFalse();
    }

    private Set<Member> memberList() {
        return new HashSet<Member>() {{
            add(new Member(5, "Bet"));
            add(new Member(6, "Gimel"));
            add(new Member(7, "Dalet"));
        }};
    }
}