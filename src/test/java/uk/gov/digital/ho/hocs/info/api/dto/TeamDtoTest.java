package uk.gov.digital.ho.hocs.info.api.dto;

import org.junit.Test;
import uk.gov.digital.ho.hocs.info.domain.model.Permission;
import uk.gov.digital.ho.hocs.info.domain.model.Team;

import java.util.HashSet;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class TeamDtoTest {

    @Test
    public void from() {

        Team team = new Team("displayName", true);

        TeamDto teamDto = TeamDto.from(team);

        assertThat(teamDto.getDisplayName()).isEqualTo("displayName");
        assertThat(teamDto.getUuid()).isEqualTo(team.getUuid());
    }
}