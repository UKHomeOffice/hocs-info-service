package uk.gov.digital.ho.hocs.info.dto;

import org.junit.Test;
import uk.gov.digital.ho.hocs.info.entities.Permission;
import uk.gov.digital.ho.hocs.info.entities.Team;

import java.util.HashSet;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class TeamDtoTest {

    UUID uuid = UUID.randomUUID();
    UUID unitUUID = UUID.randomUUID();

    @Test
    public void from() {

        Team team = new Team(1L,"displayName", uuid, unitUUID, null,new HashSet<Permission>());

        TeamDto teamDto = TeamDto.from(team);

        assertThat(teamDto.getDisplayName()).isEqualTo("displayName");
        assertThat(teamDto.getUuid()).isEqualTo(uuid);
    }
}