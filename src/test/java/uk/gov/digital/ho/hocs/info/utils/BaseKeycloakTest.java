package uk.gov.digital.ho.hocs.info.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.idm.PartialImportRepresentation;
import org.keycloak.representations.idm.RealmRepresentation;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import uk.gov.digital.ho.hocs.info.domain.repository.TeamRepository;
import uk.gov.digital.ho.hocs.info.security.KeycloakService;

import java.io.File;
import java.io.IOException;

import static org.keycloak.OAuth2Constants.CLIENT_CREDENTIALS;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles({ "local", "integration" })
@ContextConfiguration(classes = { BaseKeycloakTest.KeycloakIntegration.class })
@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
public class BaseKeycloakTest {

    @TestConfiguration
    static class KeycloakIntegration {

        @Bean("masterKeycloakClient")
        public Keycloak masterKeycloakClient(@Value("${keycloak.server.url}") String serverUrl,
                                             @Value("${keycloak.master.realm}") String realm,
                                             @Value("${keycloak.username}") String username,
                                             @Value("${keycloak.password}") String password,
                                             @Value("${keycloak.client.id}") String clientId) {
            return KeycloakBuilder.builder().serverUrl(serverUrl).realm(realm).clientId(clientId).username(
                username).password(password).build();
        }

        @Primary
        @Bean
        public Keycloak testKeycloakClient(@Value("${keycloak.server.url}") String serverUrl,
                                           @Value("${keycloak.integration.realm}") String realm,
                                           @Value("${keycloak.integration.resource}") String clientId,
                                           @Value("${keycloak.integration.secret}") String secretKey) {
            return KeycloakBuilder.builder().grantType(CLIENT_CREDENTIALS).serverUrl(serverUrl).realm(realm).clientId(
                clientId).clientSecret(secretKey).build();
        }

    }

    @Autowired
    @Qualifier("masterKeycloakClient")
    private Keycloak masterKeycloakClient;

    @Autowired
    protected Keycloak keycloakClient;

    @Autowired
    private ObjectMapper mapper;

    @Value("${keycloak.integration.realm}")
    protected String REALM;

    protected KeycloakService service;

    @Mock
    protected TeamRepository teamRepository;

    @BeforeEach
    public void createKeycloakRealm() throws IOException {
        var realmRep = new RealmRepresentation();
        realmRep.setId(REALM);
        realmRep.setRealm(REALM);
        realmRep.setEnabled(true);

        masterKeycloakClient.realms().create(realmRep);

        loadRealmInformation();

        service = new KeycloakService(teamRepository, keycloakClient, REALM);
    }

    @AfterEach
    public void cleardownKeycloakRealm() {
        masterKeycloakClient.realm(REALM).remove();
    }

    private void loadRealmInformation() throws IOException {
        var resourceFile = getClass().getResource("/local-realm-integration.json");

        assert resourceFile != null;

        var file = new File(resourceFile.getFile());
        var partialImportRep = mapper.readValue(file, PartialImportRepresentation.class);
        partialImportRep.setIfResourceExists(PartialImportRepresentation.Policy.SKIP.name());
        masterKeycloakClient.realm(REALM).partialImport(partialImportRep);
    }

}
