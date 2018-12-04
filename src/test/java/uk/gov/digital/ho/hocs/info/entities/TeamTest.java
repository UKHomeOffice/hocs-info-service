package uk.gov.digital.ho.hocs.info.entities;

import org.junit.Test;
import uk.gov.digital.ho.hocs.info.security.AccessLevel;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class TeamTest {

    @Test
    public void shouldAddPermissionsToTeam() {
        Team team = new Team("Team 1", UUID.randomUUID(), true);

        Permission permission1 = new Permission(AccessLevel.OWNER, team,
                new CaseTypeEntity(1L, "MIN", "type","a1", "role", "DCU_MIN_DISPATCH", true));
        Permission permission2 = new Permission(AccessLevel.OWNER, team,
                new CaseTypeEntity(1L, "MIN", "type","a1", "role","DCU_MIN_DISPATCH",  true));

        assertThat(team.getPermissions().size()).isEqualTo(0);
        team.addPermission(permission1);
        team.addPermission(permission2);

        assertThat(team.getPermissions()).contains(permission1);
        assertThat(team.getPermissions()).contains(permission2);

    }

}