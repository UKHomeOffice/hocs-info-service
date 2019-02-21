package uk.gov.digital.ho.hocs.info.api;

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
import org.springframework.transaction.annotation.Transactional;
import uk.gov.digital.ho.hocs.info.api.dto.PermissionDto;
import uk.gov.digital.ho.hocs.info.api.dto.TeamDto;
import uk.gov.digital.ho.hocs.info.api.dto.UpdateTeamNameRequest;
import uk.gov.digital.ho.hocs.info.api.dto.UpdateTeamPermissionsRequest;
import uk.gov.digital.ho.hocs.info.domain.repository.TeamRepository;
import uk.gov.digital.ho.hocs.info.domain.repository.UnitRepository;
import uk.gov.digital.ho.hocs.info.security.AccessLevel;
import uk.gov.digital.ho.hocs.info.security.Base64UUID;
import uk.gov.digital.ho.hocs.info.security.KeycloakService;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.SqlConfig.TransactionMode.ISOLATED;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "classpath:beforeTest.sql", config = @SqlConfig(transactionMode = ISOLATED))
@Sql(scripts = "classpath:afterTest.sql", config = @SqlConfig(transactionMode = ISOLATED), executionPhase = AFTER_TEST_METHOD)
@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
@ActiveProfiles("test")
public class UnitIntegrationTests {

    TestRestTemplate restTemplate = new TestRestTemplate();

    @Autowired
    UnitRepository unitRepository;

    private final UUID unitUUID = UUID.fromString("09221c48-b916-47df-9aa0-a0194f86f6dd");
    private HttpHeaders headers;

    @LocalServerPort
    int port;

    @Autowired
    ObjectMapper mapper;


    @Before
    public void setup() throws IOException {
        headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.toString());
    }


    @Test
    public void shouldDeleteUnit() {

        UUID unitUUID = UUID.fromString("65996106-91a5-44bf-bc92-a6c2f691f062");
        HttpEntity httpEntity = new HttpEntity(headers);
        ResponseEntity<String> result = restTemplate.exchange(
                getBasePath() + "/unit/" + unitUUID.toString()
                , HttpMethod.DELETE, httpEntity, String.class);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(unitRepository.findByUuid(unitUUID).isActive()).isFalse();
    }

    @Test
    public void shouldDeleteUnitWithInactiveTeams() {

        UUID unitUUID = UUID.fromString("10d5b353-a8ed-4530-bcc0-3edab0397d2f");
        HttpEntity httpEntity = new HttpEntity(headers);
        ResponseEntity<String> result = restTemplate.exchange(
                getBasePath() + "/unit/" + unitUUID.toString()
                , HttpMethod.DELETE, httpEntity, String.class);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(unitRepository.findByUuid(unitUUID).isActive()).isFalse();
    }

    @Test
    public void shouldReturnBadRequestAndNotDeleteUnitWhenItHasActiveTeams() {
        UUID unitUUID = UUID.fromString("09221c48-b916-47df-9aa0-a0194f86f6dd");
        HttpEntity httpEntity = new HttpEntity(headers);
        ResponseEntity<String> result = restTemplate.exchange(
                getBasePath() + "/unit/" + unitUUID.toString()
                , HttpMethod.DELETE, httpEntity, String.class);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(unitRepository.findByUuid(unitUUID).isActive()).isTrue();

    }

    private String getBasePath() {
        return "http://localhost:" + port;
    }
}


