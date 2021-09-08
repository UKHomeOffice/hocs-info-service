package uk.gov.digital.ho.hocs.info.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.entity.ContentType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.idm.PartialImportRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import uk.gov.digital.ho.hocs.info.api.dto.CaseTypeDto;
import uk.gov.digital.ho.hocs.info.security.KeycloakService;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.SqlConfig.TransactionMode.ISOLATED;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = {"user.email.whitelist=homeoffice.gov.uk"},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "classpath:beforeTest.sql", config = @SqlConfig(transactionMode = ISOLATED))
@Sql(scripts = "classpath:afterTest.sql", config = @SqlConfig(transactionMode = ISOLATED), executionPhase = AFTER_TEST_METHOD)
@ActiveProfiles("test")
public class CaseTypeIntegrationTest {

    @Autowired
    KeycloakService keycloakService;

    Keycloak keycloakClient;

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

    TestRestTemplate restTemplate = new TestRestTemplate();
    @LocalServerPort
    int port;
    @Autowired
    ObjectMapper mapper;
    private HttpHeaders headers;

    private String userId;

    @Before
    public void setup() throws IOException {
        headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.toString());
        keycloakClient = Keycloak.getInstance(
                serverUrl, "master", username, password, clientId, clientId);
        setupKeycloakRealm();

//        mockCaseworkService = buildMockService(restTemplate);
        userId = keycloakClient.realm(HOCS_REALM).users().search("admin").get(0).getId();
    }

    @Test
    public void shouldRetrieveAllCaseTypesWithoutPreviousCaseType() {

        // given
        // setup done in before.sql
        HttpEntity httpEntity = new HttpEntity(headers);

        // when
        ResponseEntity<Set<CaseTypeDto>> getCaseTypesRequest = restTemplate.exchange(
                getBasePath() + "/caseType", HttpMethod.GET, httpEntity, new ParameterizedTypeReference<>() {
                });

        // then
        assertThat(getCaseTypesRequest).isNotNull();
        assertThat(getCaseTypesRequest.getStatusCodeValue()).isEqualTo(200);

        Set<CaseTypeDto> caseTypeDtos = getCaseTypesRequest.getBody();
        assertThat(caseTypeDtos).isNotNull();
        assertThat(caseTypeDtos
                .stream()
                .filter(caseTypeDto -> caseTypeDto.getPreviousCaseType() == null)
                .count())
                .isEqualTo(3);

    }

    @Test
    public void shouldRetrieveAllCaseTypesWithPreviousCaseType() {

        // given
        // setup done in before.sql
        HttpEntity httpEntity = new HttpEntity(headers);

        // when
        ResponseEntity<Set<CaseTypeDto>> getCaseTypesRequest = restTemplate.exchange(
                getBasePath() + "/caseType?addCasesWithPreviousType=true", HttpMethod.GET, httpEntity, new ParameterizedTypeReference<>() {
                });

        // then
        assertThat(getCaseTypesRequest).isNotNull();
        assertThat(getCaseTypesRequest.getStatusCodeValue()).isEqualTo(200);

        Set<CaseTypeDto> caseTypeDtos = getCaseTypesRequest.getBody();
        assertThat(caseTypeDtos).isNotNull();
        assertThat(caseTypeDtos.size()).isEqualTo(4);

    }

    @Test
    public void shouldRetrieveNonBulkCasesWithoutPreviousCaseType() {
        // given
        headers.add("X-Auth-Groups", "/CGEvBrriTS-Q0iJUpoQUuA,/kRravlq3RHCDlWtYSmFGLQ,/Q0pOM0N_Tm2PBBTqQP2_og,/iztDZqN8SLaydExQ-Ag4Qw,/XVhBKWbqTpeSd3V2qx0ywA,/fDPIeJQET2ebvMpS3_KFyg,/0J8URIfsQZeOxfKPVI0Rvg");
        // setup done in before.sql
        HttpEntity httpEntity = new HttpEntity(headers);

        Map<Boolean, Long> bulkValues = Map.of(Boolean.FALSE, 3L, Boolean.TRUE, 3L);

        for (Map.Entry bulkValue : bulkValues.entrySet()) {
            // when
            ResponseEntity<Set<CaseTypeDto>> getCaseTypesRequest = restTemplate.exchange(
                    getBasePath() + "/caseType?bulkOnly=" + bulkValue.getKey(), HttpMethod.GET, httpEntity, new ParameterizedTypeReference<>() {
                    });

            // then
            assertThat(getCaseTypesRequest).isNotNull();
            assertThat(getCaseTypesRequest.getStatusCodeValue()).isEqualTo(200);

            Set<CaseTypeDto> caseTypeDtos = getCaseTypesRequest.getBody();
            assertThat(caseTypeDtos).isNotNull();
            assertThat(caseTypeDtos
                    .stream()
                    .filter(caseTypeDto -> caseTypeDto.getPreviousCaseType() == null)
                    .count())
                    .isEqualTo(bulkValue.getValue());

        }

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

}


