package uk.gov.digital.ho.hocs.info.entities;

import org.junit.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


public class UnitTest {

    @Test
    public void shouldAddTeamsToEntity() {
        UUID team1UUID = UUID.randomUUID();
        UUID team2UUID = UUID.randomUUID();
        Unit unit = new Unit("Unit 1", "TEST", UUID.randomUUID(), true);

        Team team1 = new Team("Team 2", team1UUID);
        Team team2 = new Team("Team 2", team2UUID);

        assertThat(unit.getTeams().size()).isEqualTo(0);
        unit.addTeam(team1);
        unit.addTeam(team2);

        assertThat(unit.getTeams()).contains(team1);
        assertThat(unit.getTeams()).contains(team2);
    }

    @Test
    public void shouldRemoveTeam() {
        UUID team1UUID = UUID.randomUUID();
        UUID team2UUID = UUID.randomUUID();
        Unit unit = new Unit("Unit 1", "TEST", UUID.randomUUID(), true);

        Team team1 = new Team("Team 2", team1UUID);
        Team team2 = new Team("Team 2", team2UUID);
        unit.addTeam(team1);
        unit.addTeam(team2);

        unit.removeTeam(team2UUID);

        assertThat(unit.getTeams()).contains(team1);
        assertThat(unit.getTeams()).doesNotContain(team2);

    }
}