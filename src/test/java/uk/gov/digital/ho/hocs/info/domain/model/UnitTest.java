package uk.gov.digital.ho.hocs.info.domain.model;

import org.junit.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


public class UnitTest {

    @Test
    public void shouldAddTeamsToEntity() {
        Unit unit = new Unit("Unit 1", "TEST", true);

        Team team1 = new Team("Team 2", true);
        Team team2 = new Team("Team 2", true);

        assertThat(unit.getTeams().size()).isEqualTo(0);
        unit.addTeam(team1);
        unit.addTeam(team2);

        assertThat(unit.getTeams()).contains(team1);
        assertThat(unit.getTeams()).contains(team2);
    }

    @Test
    public void shouldRemoveTeam() {
        Unit unit = new Unit("Unit 1", "TEST", true);

        Team team1 = new Team("Team 2", true);
        Team team2 = new Team("Team 2", true);
        unit.addTeam(team1);
        unit.addTeam(team2);

        unit.removeTeam(team2.getUuid());

        assertThat(unit.getTeams()).contains(team1);
        assertThat(unit.getTeams()).doesNotContain(team2);

    }
}