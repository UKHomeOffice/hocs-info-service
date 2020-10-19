package uk.gov.digital.ho.hocs.info.api.dto;

import org.junit.Test;
import uk.gov.digital.ho.hocs.info.domain.model.Team;
import uk.gov.digital.ho.hocs.info.domain.model.TopicTeam;

import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class TopicTeamDtoTest {

    UUID uuid = UUID.randomUUID();

    @Test
    public void from() {
        Set<Team> teams = Set.of(new Team("Team", true));
        TopicTeam topicTeam = new TopicTeam(uuid,"DisplayName",teams);

        TopicTeamDto topicTeamDto = TopicTeamDto.from(topicTeam);

        assertThat(topicTeamDto.getUuid()).isEqualTo(uuid);
        assertThat(topicTeamDto.getDisplayName()).isEqualTo("DisplayName");
        assertThat(topicTeamDto.getTeams().size()).isEqualTo(1);
    }
}
