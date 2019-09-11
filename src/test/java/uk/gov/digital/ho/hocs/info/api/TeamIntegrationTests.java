package uk.gov.digital.ho.hocs.info.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.entity.ContentType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.representations.idm.PartialImportRepresentation;
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
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import uk.gov.digital.ho.hocs.info.api.dto.*;
import uk.gov.digital.ho.hocs.info.domain.repository.TeamRepository;
import uk.gov.digital.ho.hocs.info.security.AccessLevel;
import uk.gov.digital.ho.hocs.info.security.Base64UUID;
import uk.gov.digital.ho.hocs.info.security.KeycloakService;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.SqlConfig.TransactionMode.ISOLATED;
import static org.springframework.test.web.client.MockRestServiceServer.bindTo;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "classpath:beforeTest.sql", config = @SqlConfig(transactionMode = ISOLATED))
@Sql(scripts = "classpath:afterTest.sql", config = @SqlConfig(transactionMode = ISOLATED), executionPhase = AFTER_TEST_METHOD)
@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
@ActiveProfiles("test")
public class TeamIntegrationTests {

    TestRestTemplate testRestTemplate = new TestRestTemplate();

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    TeamRepository teamRepository;

    @Autowired
    KeycloakService keycloakService;

    Keycloak keycloakClient;

    private MockRestServiceServer mockCaseworkService;

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

    private String userId;
    private final UUID unitUUID = UUID.fromString("09221c48-b916-47df-9aa0-a0194f86f6dd");


    @Before
    public void setup() throws IOException {
        headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.toString());
        keycloakClient = Keycloak.getInstance(
                serverUrl, "master", username, password, clientId, clientId);
        setupKeycloakRealm();

        mockCaseworkService = buildMockService(restTemplate);
        userId = keycloakClient.realm(HOCS_REALM).users().search("admin").get(0).getId();
    }


    @Test
    public void shouldGetAllTeamsForUnit() {
        HttpEntity httpEntity = new HttpEntity(headers);
        ResponseEntity<Set<TeamDto>> result = testRestTemplate.exchange(
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

        ResponseEntity<TeamDto> result = testRestTemplate.exchange(
                getBasePath() + "/unit/" + unitUUID.toString() + "/teams"
                , HttpMethod.POST, httpEntity, TeamDto.class);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(teamRepository.findByUuid(result.getBody().getUuid())).isNotNull();

        GroupRepresentation group = keycloakClient.realm("hocs")
                .getGroupByPath("/" + Base64UUID.UUIDToBase64String(result.getBody().getUuid()));

        assertThat(group).isNotNull();
    }

    @Test
    public void shouldAddUserToGroup() {

        String teamId = "434a4e33-437f-4e6d-8f04-14ea40fdbfa2";
        String base64TeamUUID = Base64UUID.UUIDToBase64String(UUID.fromString(teamId));
        HttpEntity httpEntity = new HttpEntity(headers);

        ResponseEntity<String> result = testRestTemplate.exchange(
                getBasePath() + "/users/" + userId + "/team/" + teamId
                , HttpMethod.POST, httpEntity, String.class);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);

        GroupRepresentation group = keycloakClient.realm(HOCS_REALM)
                .getGroupByPath("/" + base64TeamUUID);

        assertThat(keycloakClient.realm(HOCS_REALM)
                .users().get(userId).groups().stream()
                .anyMatch(g -> g.getId().equals(group.getId()))).isTrue();

    }

    @Test
    public void shouldRemoveUserFromGroup() throws JsonProcessingException {

        String teamId = "434a4e33-437f-4e6d-8f04-14ea40fdbfa2";
        String base64TeamUUID = Base64UUID.UUIDToBase64String(UUID.fromString(teamId));
        HttpEntity httpEntity = new HttpEntity(headers);

        setupCaseworkServiceWithNoCases();

        GroupRepresentation group = keycloakClient.realm(HOCS_REALM)
                .getGroupByPath("/" + base64TeamUUID);

        assertThat(keycloakClient.realm(HOCS_REALM)
                .users().get(userId).groups().stream()
                .anyMatch(g -> g.getId().equals(group.getId()))).isTrue();

        ResponseEntity<String> result = testRestTemplate.exchange(
                getBasePath() + "/users/" + userId + "/team/" + teamId
                , HttpMethod.DELETE, httpEntity, String.class);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);

        assertThat(keycloakClient.realm(HOCS_REALM)
                .users().get(userId).groups().stream()
                .anyMatch(g -> g.getId().equals(group.getId()))).isFalse();
    }

    @Test
    public void shouldNotRemoveUserFromGroupWhenUserHasCases() throws JsonProcessingException {

        String teamId = "434a4e33-437f-4e6d-8f04-14ea40fdbfa2";
        String base64TeamUUID = Base64UUID.UUIDToBase64String(UUID.fromString(teamId));
        HttpEntity httpEntity = new HttpEntity(headers);

        setupCaseworkServiceWithCases();

        GroupRepresentation group = keycloakClient.realm(HOCS_REALM)
                .getGroupByPath("/" + base64TeamUUID);

        assertThat(keycloakClient.realm(HOCS_REALM)
                .users().get(userId).groups().stream()
                .anyMatch(g -> g.getId().equals(group.getId()))).isTrue();

        ResponseEntity<String> result = testRestTemplate.exchange(
                getBasePath() + "/users/" + userId + "/team/" + teamId
                , HttpMethod.DELETE, httpEntity, String.class);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);

        assertThat(keycloakClient.realm(HOCS_REALM)
                .users().get(userId).groups().stream()
                .anyMatch(g -> g.getId().equals(group.getId()))).isTrue();
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

        ResponseEntity<String> result = testRestTemplate.exchange(
                getBasePath() + "/team/" + teamId + "/permissions"
                , HttpMethod.DELETE, httpEntity, String.class);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
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

        ResponseEntity<String> result = testRestTemplate.exchange(
                getBasePath() + "/team/" + teamId + "/permissions"
                , HttpMethod.DELETE, httpEntity, String.class);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(teamRepository.findByUuid(UUID.fromString(teamId)).getPermissions()).size().isEqualTo(1);
    }

    @Test
    public void shouldChangeTeamName() {
        String teamId = "08612f06-bae2-4d2f-90d2-2254a68414b8";

        UpdateTeamNameRequest request = new UpdateTeamNameRequest("New Team Name");
        HttpEntity<UpdateTeamNameRequest> httpEntity = new HttpEntity(request);

        ResponseEntity<String> result = testRestTemplate.exchange(
                getBasePath() + "/team/" + teamId
                , HttpMethod.PUT, httpEntity, String.class);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(teamRepository.findByUuid(UUID.fromString(teamId)).getDisplayName()).isEqualTo("New Team Name");
    }

    @Test
    public void shouldChangeTeamLetterName() {
        String teamId = "08612f06-bae2-4d2f-90d2-2254a68414b8";

        UpdateTeamLetterNameRequest request = new UpdateTeamLetterNameRequest("New Name");
        HttpEntity<UpdateTeamNameRequest> httpEntity = new HttpEntity(request);

        ResponseEntity<String> result = testRestTemplate.exchange(
                getBasePath() + "/team/" + teamId +"/lettername"
                , HttpMethod.PUT, httpEntity, String.class);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(teamRepository.findByUuid(UUID.fromString(teamId)).getLetterName()).isEqualTo("New Name");
    }

    @Test
    public void shouldDeleteTeam() {
        String teamId = "8b3b4366-a37c-48b6-b274-4c50f8083843";

        ResponseEntity<String> result = testRestTemplate.exchange(
                getBasePath() + "/team/" + teamId
                , HttpMethod.DELETE, new HttpEntity(null), String.class);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(teamRepository.findByUuid(UUID.fromString(teamId)).isActive()).isFalse();
    }

    @Test
    public void shouldReturnPreConditionFailedErrorWhenTryingToDeleteTeamWhichHasActiveParentTopicsAttached() {
        String teamId = "7c33c878-9404-4f67-9bbc-ca52dff285ca";

        ResponseEntity<String> result = testRestTemplate.exchange(
                getBasePath() + "/team/" + teamId
                , HttpMethod.DELETE, new HttpEntity(null), String.class);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.PRECONDITION_FAILED);
        assertThat(teamRepository.findByUuid(UUID.fromString(teamId)).isActive()).isTrue();
    }

    @Test
    public void shouldReturnTeamForCaseByRegionAndStage(){
        String caseUUID = "08d72d00-f081-4156-96b1-c36e511012ba";
        String regionUUID = "54321111-eeee-8436-3692-16782938a620";
        String stageType = "ST1";
        HttpEntity httpEntity = new HttpEntity(headers);

        ResponseEntity<TeamDto> result = testRestTemplate.exchange(
                getBasePath() + "/team/case/" + caseUUID + "/region/" + regionUUID + "/stage/" + stageType
                , HttpMethod.GET, httpEntity, new ParameterizedTypeReference<TeamDto>() {
                });

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody().getUuid().toString()).isEqualTo("5d584129-66ea-4e97-9277-7576ab1d32c0");
    }


    private String getBasePath() {
        return "http://localhost:" + port;
    }

    private void setupKeycloakRealm() throws IOException {
        RealmResource hocsRealm = keycloakClient.realm(HOCS_REALM);

        PartialImportRepresentation importRealm = mapper.readValue(new File("./keycloak/local-realm.json"), PartialImportRepresentation.class);
        hocsRealm.groups().groups().stream().forEach(e -> hocsRealm.groups().group(e.getId()).remove());
        importRealm.setIfResourceExists(PartialImportRepresentation.Policy.OVERWRITE.toString());
        keycloakClient.realm(HOCS_REALM).partialImport(importRealm);
    }

    private MockRestServiceServer buildMockService(RestTemplate restTemplate) {
        MockRestServiceServer.MockRestServiceServerBuilder infoBuilder = bindTo(restTemplate);
        infoBuilder.ignoreExpectOrder(true);
        return infoBuilder.build();
    }

    private void setupCaseworkServiceWithNoCases() throws JsonProcessingException {
        String teamUUID = "434a4e33-437f-4e6d-8f04-14ea40fdbfa2";
        Set<UUID> caseUUIDs = new HashSet<>();

        mockCaseworkService
                .expect(requestTo("http://localhost:8082/stage/team/" + teamUUID + "/user/" + userId))
                .andExpect(method(GET))
                .andRespond(withSuccess(mapper.writeValueAsString(caseUUIDs), MediaType.APPLICATION_JSON));
    }

    private void setupCaseworkServiceWithCases() throws JsonProcessingException {
        String teamUUID = "434a4e33-437f-4e6d-8f04-14ea40fdbfa2";
        Set<UUID> caseUUIDs = new HashSet<>();
        caseUUIDs.add(UUID.fromString("08d72d00-f081-4156-96b1-c36e511012ba"));

        mockCaseworkService
                .expect(requestTo("http://localhost:8082/stage/team/" + teamUUID + "/user/" + userId))
                .andExpect(method(GET))
                .andRespond(withSuccess(mapper.writeValueAsString(caseUUIDs), MediaType.APPLICATION_JSON));
    }
}