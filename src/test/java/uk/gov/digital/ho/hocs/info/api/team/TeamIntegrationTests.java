package uk.gov.digital.ho.hocs.info.api.team;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.entity.ContentType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.representations.idm.RealmRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.digital.ho.hocs.info.api.dto.*;
import uk.gov.digital.ho.hocs.info.domain.repository.TeamRepository;
import uk.gov.digital.ho.hocs.info.security.AccessLevel;
import uk.gov.digital.ho.hocs.info.security.KeycloakService;
import javax.ws.rs.NotFoundException;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.SqlConfig.TransactionMode.ISOLATED;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "classpath:beforeTest.sql", config = @SqlConfig(transactionMode = ISOLATED))
@Sql(scripts = "classpath:afterTest.sql", config = @SqlConfig(transactionMode = ISOLATED), executionPhase = AFTER_TEST_METHOD)
@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
@ActiveProfiles("test")
public class TeamIntegrationTests {

    TestRestTemplate restTemplate = new TestRestTemplate();

    @Autowired
    TeamRepository teamRepository;

    @Autowired
    KeycloakService keycloakService;


    Keycloak keycloakClient;

    private final UUID unitUUID = UUID.fromString("09221c48-b916-47df-9aa0-a0194f86f6dd");
    private HttpHeaders headers;

    @LocalServerPort
    int port;

    @Autowired
    ObjectMapper mapper;

    @Value("${keycloak.server.url}")
    String serverUrl;
    @Value("${keycloak.username}")
    String username;
    @Value("${keycloak.password}")
    String password;
    @Value("${keycloak.client.id}")
    String clientId;
    @Value("${keycloak.realm}")
    String HOCS_REALM;


    @Before
    public void setup() throws IOException {
        headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.toString());
        keycloakClient = Keycloak.getInstance(
                serverUrl, "master", username, password, clientId, clientId);
        setupKeycloakRealm();
    }

    @Test
    public void shouldAddTeamToUnitAndRemoveFromOldUnit() {

        String teamUUID = "434a4e33-437f-4e6d-8f04-14ea40fdbfa2";
        UUID newUnitUUID = UUID.fromString("65996106-91a5-44bf-bc92-a6c2f691f062");
        HttpEntity httpEntity = new HttpEntity(headers);


        keycloakClient.realm(HOCS_REALM)
                .getGroupByPath("/UNIT2/" + teamUUID + "/CT2/WRITE");

        assertThatThrownBy(() -> keycloakClient.realm(HOCS_REALM)
                .getGroupByPath("/UNIT3/" + teamUUID + "/CT2/WRITE")).isInstanceOf(NotFoundException.class);

        ResponseEntity<String> result = restTemplate.exchange(
                getBasePath() + "/unit/" + newUnitUUID + "/teams/" + teamUUID
                , HttpMethod.POST, httpEntity, String.class);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);

        assertThat(teamRepository.findByUuid(UUID.fromString(teamUUID)).getUnit().getUuid()).isEqualTo(newUnitUUID);


        keycloakClient.realm(HOCS_REALM)
                .getGroupByPath("/UNIT3/" + teamUUID + "/CT2/WRITE");

        assertThatThrownBy(() -> keycloakClient.realm(HOCS_REALM)
                .getGroupByPath("/UNIT2/" + teamUUID + "/CT2/WRITE")).isInstanceOf(NotFoundException.class);

    }


    @Test
    public void shouldGetAllTeams() {
        HttpEntity httpEntity = new HttpEntity(headers);
        ResponseEntity<Set<TeamDto>> result = restTemplate.exchange(
                getBasePath() + "/unit/" + unitUUID.toString() + "/teams"
                , HttpMethod.GET, httpEntity, new ParameterizedTypeReference<Set<TeamDto>>() {
                });
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody().size()).isEqualTo(6);
    }

    @Test
    public void shouldAddTeamToDatabaseAndKeyCloak() {

        Set<PermissionDto> permissions = new HashSet<PermissionDto>() {{
            add(new PermissionDto("CT1", AccessLevel.OWNER));
        }};
        TeamDto team = new TeamDto("Team 3000", permissions);

        HttpEntity<TeamDto> httpEntity = new HttpEntity<>(team, headers);

        ResponseEntity<TeamDto> result = restTemplate.exchange(
                getBasePath() + "/unit/" + unitUUID.toString() + "/teams"
                , HttpMethod.POST, httpEntity, TeamDto.class);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(teamRepository.findByUuid(result.getBody().getUuid())).isNotNull();
        GroupRepresentation unitGroup = keycloakClient.realm("hocs")
                .getGroupByPath("/UNIT2/" + team.getUuid() + "/CT1/OWNER");
        assertThat(unitGroup).isNotNull();
    }

    @Test
    public void shouldAddUserToGroup() {

        String userId = "ed00bf4a-1a74-4d0b-a3d0-379c12c5e3ff";
        String teamId = "08612f06-bae2-4d2f-90d2-2254a68414b8";
        HttpEntity httpEntity = new HttpEntity(headers);

        ResponseEntity<String> result = restTemplate.exchange(
                getBasePath() + "/users/" + userId + "/team/" + teamId
                , HttpMethod.POST, httpEntity, String.class);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);

        GroupRepresentation group = keycloakClient.realm(HOCS_REALM)
                .getGroupByPath("/UNIT2/" + teamId + "/CT1/OWNER");

        assertThat(keycloakClient.realm(HOCS_REALM)
                .users().get(userId).groups().stream()
                .anyMatch(g -> g.getId().equals(group.getId()))).isTrue();

        GroupRepresentation mainGroup = keycloakClient.realm(HOCS_REALM)
                .getGroupByPath("/UNIT2/"+ teamId);
        assertThat(keycloakClient.realm(HOCS_REALM)
                .users().get(userId).groups().stream()
                .anyMatch(g -> g.getId().equals(mainGroup.getId()))).isTrue();
    }

    @Test
    public void shouldUpdateTeamPermissions() {
        String teamId = "434a4e33-437f-4e6d-8f04-14ea40fdbfa2";
        Set<PermissionDto> permissions = new HashSet<PermissionDto>() {{
            add(new PermissionDto("CT2", AccessLevel.READ));
        }};

        UpdateTeamPermissionsRequest request = new UpdateTeamPermissionsRequest(permissions);

        HttpEntity<UpdateTeamPermissionsRequest> httpEntity = new HttpEntity<>(request, headers);

        ResponseEntity<String> result = restTemplate.exchange(
                getBasePath() + "/team/" + teamId + "/permissions"
                , HttpMethod.PUT, httpEntity, String.class);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);

        GroupRepresentation permissionGroup = keycloakClient.realm(HOCS_REALM)
                .getGroupByPath("/UNIT2/" + teamId + "/CT2/READ");
        assertThat(permissionGroup).isNotNull();

    }

    @Test
    @Transactional
    public void shouldDeleteTeamPermission() {
        String teamId = "434a4e33-437f-4e6d-8f04-14ea40fdbfa2";
        Set<PermissionDto> permissions = new HashSet<PermissionDto>() {{
            add(new PermissionDto("CT2", AccessLevel.WRITE));
        }};

        UpdateTeamPermissionsRequest request = new UpdateTeamPermissionsRequest(permissions);

        HttpEntity<UpdateTeamPermissionsRequest> httpEntity = new HttpEntity<>(request, headers);

        ResponseEntity<String> result = restTemplate.exchange(
                getBasePath() + "/team/" + teamId + "/permissions"
                , HttpMethod.DELETE, httpEntity, String.class);


        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);

        assertThatThrownBy(() -> keycloakClient.realm(HOCS_REALM)
                .getGroupByPath("/UNIT2/" + teamId + "/CT2/WRITE")).isInstanceOf(NotFoundException.class);

        assertThat(teamRepository.findByUuid(UUID.fromString(teamId)).getPermissions()).size().isEqualTo(0);
    }

    @Test
    @Transactional
    public void shouldDeleteOnePermissionForTeam() {
        String teamId = "8b3b4366-a37c-48b6-b274-4c50f8083843";
        Set<PermissionDto> permissions = new HashSet<PermissionDto>() {{
            add(new PermissionDto("CT3", AccessLevel.WRITE));
        }};

        UpdateTeamPermissionsRequest request = new UpdateTeamPermissionsRequest(permissions);

        HttpEntity<UpdateTeamPermissionsRequest> httpEntity = new HttpEntity<>(request, headers);

        ResponseEntity<String> result = restTemplate.exchange(
                getBasePath() + "/team/" + teamId + "/permissions"
                , HttpMethod.DELETE, httpEntity, String.class);


        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);

        assertThatThrownBy(() -> keycloakClient.realm(HOCS_REALM)
                .getGroupByPath("/UNIT2/" + teamId + "/CT3/WRITE")).isInstanceOf(NotFoundException.class);

        GroupRepresentation permissionGroup = keycloakClient.realm(HOCS_REALM)
                .getGroupByPath("/UNIT2/" + teamId + "/CT3/READ");
        assertThat(permissionGroup).isNotNull();

        assertThat(teamRepository.findByUuid(UUID.fromString(teamId)).getPermissions()).size().isEqualTo(1);
    }


    @Test
    public void shouldUpdateUserPermissionsOnTeamUpdate() {
        String teamId = "434a4e33-437f-4e6d-8f04-14ea40fdbfa2";
        Set<PermissionDto> permissions = new HashSet<PermissionDto>() {{
            add(new PermissionDto("CT2", AccessLevel.READ));
        }};

        UpdateTeamPermissionsRequest request = new UpdateTeamPermissionsRequest(permissions);

        HttpEntity<UpdateTeamPermissionsRequest> httpEntity = new HttpEntity<>(request, headers);

        ResponseEntity<String> result = restTemplate.exchange(
                getBasePath() + "/team/" + teamId + "/permissions"
                , HttpMethod.PUT, httpEntity, String.class);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);

        GroupRepresentation permissionGroup = keycloakClient.realm(HOCS_REALM)
                .getGroupByPath("/UNIT2/" + teamId + "/CT2/READ");

        assertThat(keycloakClient.realm(HOCS_REALM).groups().group(permissionGroup.getId()).members().get(0).getId())
                .isEqualTo("ed00bf4a-1a74-4d0b-a3d0-379c12c5e3ff");

    }

    @Test
    public void shouldChangeTeamName() {
        String teamId = "08612f06-bae2-4d2f-90d2-2254a68414b8";

        UpdateTeamNameRequest request = new UpdateTeamNameRequest("New Team Name");
        HttpEntity<UpdateTeamNameRequest> httpEntity = new HttpEntity(request);

        ResponseEntity<String> result = restTemplate.exchange(
                getBasePath() + "/team/" + teamId
                , HttpMethod.PUT, httpEntity, String.class);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(teamRepository.findByUuid(UUID.fromString(teamId)).getDisplayName()).isEqualTo("New Team Name");
    }

    @Test
    public void shouldDeleteTeam() {
        String teamId =  "5d584129-66ea-4e97-9277-7576ab1d32c0";

        ResponseEntity<String> result = restTemplate.exchange(
                getBasePath() + "/team/" + teamId
                , HttpMethod.DELETE,new HttpEntity(null) ,String.class);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(teamRepository.findByUuid(UUID.fromString(teamId)).isActive()).isFalse();
    }

    @Test
    public void shouldReturnPreConditionFailedErrorWhenTryingToDeleteTeamWhichHasActiveParentTopicsAttached() {
        String teamId =  "7c33c878-9404-4f67-9bbc-ca52dff285ca";

        ResponseEntity<String> result = restTemplate.exchange(
                getBasePath() + "/team/" + teamId
                , HttpMethod.DELETE,new HttpEntity(null) ,String.class);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.PRECONDITION_FAILED);
        assertThat(teamRepository.findByUuid(UUID.fromString(teamId)).isActive()).isTrue();
    }


    private String getBasePath() {
        return "http://localhost:" + port;
    }

    private void setupKeycloakRealm() throws IOException {
     try {
         keycloakClient.realms().realm(HOCS_REALM).remove();
        } catch (Exception e) {
            //Realm does not exist
        }
        RealmRepresentation hocsRealm = mapper.readValue(new File("./keycloak/local-realm.json"), RealmRepresentation.class);
        keycloakClient.realms().create(hocsRealm);
    }
}


