package uk.gov.digital.ho.hocs.info.domain.model;

import org.junit.Test;
import uk.gov.digital.ho.hocs.info.security.AccessLevel;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class TeamTest {

    @Test
    public void shouldAddPermissionsToTeam() {
        Team team = new Team("Team 1", UUID.randomUUID(), true);

        Permission permission1 = new Permission(AccessLevel.OWNER, team,
                new CaseType(1L,UUID.randomUUID(), "MIN", "type","a1", UUID.randomUUID(), "DCU_MIN_DISPATCH", true,true));
        Permission permission2 = new Permission(AccessLevel.OWNER, team,
                new CaseType(1L,UUID.randomUUID(), "MIN", "type","a1", UUID.randomUUID(),"DCU_MIN_DISPATCH",  true, true));

        assertThat(team.getPermissions().size()).isEqualTo(0);
        team.addPermission(permission1);
        team.addPermission(permission2);

        assertThat(team.getPermissions()).contains(permission1);
        assertThat(team.getPermissions()).contains(permission2);

    }

}