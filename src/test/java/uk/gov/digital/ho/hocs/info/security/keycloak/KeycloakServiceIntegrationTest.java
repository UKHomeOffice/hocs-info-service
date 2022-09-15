package uk.gov.digital.ho.hocs.info.security.keycloak;

import org.junit.jupiter.api.Test;
import org.keycloak.representations.idm.UserRepresentation;
import uk.gov.digital.ho.hocs.info.domain.model.Team;
import uk.gov.digital.ho.hocs.info.utils.BaseKeycloakTest;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class KeycloakServiceIntegrationTest extends BaseKeycloakTest {

    @Test
    public void shouldGetAllUsers() {
        List<UserRepresentation> result = service.getAllUsers();

        assertThat(result).hasSize(110);
    }

    @Test
    public void shouldGetAllUsersInTeam() {
        when(teamRepository.findByUuid(UUID.fromString("00000000-0000-0000-0000-000000000000"))).thenReturn(
            new Team("", Collections.emptySet()));

        List<UserRepresentation> result = service.getUsersForTeam(
            UUID.fromString("00000000-0000-0000-0000-000000000000"));

        assertThat(result).hasSize(110);
    }

}
