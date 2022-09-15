package uk.gov.digital.ho.hocs.info.api;

import org.apache.http.entity.ContentType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import uk.gov.digital.ho.hocs.info.api.dto.CaseTypeActionDto;
import uk.gov.digital.ho.hocs.info.api.dto.CaseTypeDto;

import java.io.IOException;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.SqlConfig.TransactionMode.ISOLATED;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = { "user.email.whitelist=homeoffice.gov.uk" },
                webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "classpath:beforeTest.sql", config = @SqlConfig(transactionMode = ISOLATED))
@Sql(scripts = "classpath:afterTest.sql",
     config = @SqlConfig(transactionMode = ISOLATED),
     executionPhase = AFTER_TEST_METHOD)
@ActiveProfiles({ "local", "integration" })
public class CaseTypeIntegrationTest {

    TestRestTemplate restTemplate = new TestRestTemplate();

    @LocalServerPort
    int port;

    private HttpHeaders headers;

    @Before
    public void setup() throws IOException {
        headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.toString());
    }

    @Test
    public void shouldRetrieveAllInitialCaseTypes() {
        // given
        // setup done in before.sql
        HttpEntity httpEntity = new HttpEntity(headers);

        // when
        ResponseEntity<Set<CaseTypeDto>> getCaseTypesRequest = restTemplate.exchange(
            getBasePath() + "/caseType?initialCaseType=true", HttpMethod.GET, httpEntity,
            new ParameterizedTypeReference<>() {});

        // then
        assertThat(getCaseTypesRequest).isNotNull();
        assertThat(getCaseTypesRequest.getStatusCodeValue()).isEqualTo(200);

        Set<CaseTypeDto> caseTypeDtos = getCaseTypesRequest.getBody();
        assertThat(caseTypeDtos).isNotNull();
        assertThat(
            caseTypeDtos.stream().filter(caseTypeDto -> caseTypeDto.getPreviousCaseType() == null).count()).isEqualTo(
            3);

    }

    @Test
    public void shouldRetrieveAllCaseTypes() {

        // given
        // setup done in before.sql
        HttpEntity httpEntity = new HttpEntity(headers);

        // when
        ResponseEntity<Set<CaseTypeDto>> getCaseTypesRequest = restTemplate.exchange(
            getBasePath() + "/caseType?addCasesWithPreviousType=true", HttpMethod.GET, httpEntity,
            new ParameterizedTypeReference<>() {});

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
        headers.add("X-Auth-Groups",
            "/CGEvBrriTS-Q0iJUpoQUuA,/kRravlq3RHCDlWtYSmFGLQ,/Q0pOM0N_Tm2PBBTqQP2_og,/iztDZqN8SLaydExQ-Ag4Qw,/XVhBKWbqTpeSd3V2qx0ywA,/fDPIeJQET2ebvMpS3_KFyg,/0J8URIfsQZeOxfKPVI0Rvg");
        // setup done in before.sql
        HttpEntity httpEntity = new HttpEntity(headers);

        Map<Boolean, Long> bulkValues = Map.of(Boolean.FALSE, 3L, Boolean.TRUE, 3L);

        for (Map.Entry<Boolean, Long> bulkValue : bulkValues.entrySet()) {
            // when
            ResponseEntity<Set<CaseTypeDto>> getCaseTypesRequest = restTemplate.exchange(
                getBasePath() + "/caseType?bulkOnly=" + bulkValue.getKey(), HttpMethod.GET, httpEntity,
                new ParameterizedTypeReference<>() {});

            // then
            assertThat(getCaseTypesRequest).isNotNull();
            assertThat(getCaseTypesRequest.getStatusCodeValue()).isEqualTo(200);

            Set<CaseTypeDto> caseTypeDtos = getCaseTypesRequest.getBody();
            assertThat(caseTypeDtos).isNotNull();
            assertThat(caseTypeDtos.stream().filter(
                caseTypeDto -> caseTypeDto.getPreviousCaseType() == null).count()).isEqualTo(bulkValue.getValue());

        }

    }

    @Test
    public void shouldGetAllCaseActionsForCaseType() {
        String caseTypeString = "CT1";

        ParameterizedTypeReference<List<CaseTypeActionDto>> typeReference = new ParameterizedTypeReference<>() {};

        HttpEntity httpEntity = new HttpEntity(headers);
        ResponseEntity<List<CaseTypeActionDto>> response = restTemplate.exchange(
            getBasePath() + "/caseType/" + caseTypeString + "/actions", HttpMethod.GET, httpEntity, typeReference);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Objects.requireNonNull(response.getBody()).size()).isEqualTo(2);
    }

    @Test
    public void shouldReturnEmptyArrayWhenNoActionsForCaseType() {
        String caseTypeString = "CASE_NON_EXISTENT";

        ParameterizedTypeReference<List<CaseTypeActionDto>> typeReference = new ParameterizedTypeReference<>() {};

        HttpEntity httpEntity = new HttpEntity(headers);
        ResponseEntity<List<CaseTypeActionDto>> response = restTemplate.exchange(
            getBasePath() + "/caseType/" + caseTypeString + "/actions", HttpMethod.GET, httpEntity, typeReference);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Objects.requireNonNull(response.getBody()).size()).isEqualTo(0);
    }

    @Test
    public void shouldReturnExceptionWhenActionIdNotExist() {
        String caseTypeString = "CT1";
        UUID nonExistentActionID = UUID.randomUUID();

        ResponseEntity<Void> response = restTemplate.exchange(
            getBasePath() + "/caseType/" + caseTypeString + "/actions/" + nonExistentActionID, HttpMethod.GET,
            new HttpEntity<>(headers), Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

    }

    @Test
    public void shouldReturnRequestedActionById() {
        String caseTypeString = "CT1";

        String actionID = "f2b625c9-7250-4293-9e68-c8f515e3043d";

        ParameterizedTypeReference<CaseTypeActionDto> typeReference = new ParameterizedTypeReference<>() {};

        HttpEntity httpEntity = new HttpEntity(headers);
        ResponseEntity<CaseTypeActionDto> response = restTemplate.exchange(
            getBasePath() + "/caseType/" + caseTypeString + "/actions/" + actionID, HttpMethod.GET, httpEntity,
            typeReference);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("APPEAL 1", Objects.requireNonNull(response.getBody()).getActionLabel());

    }

    @Test
    public void shouldReturnAllCaseTypeActions() {
        ParameterizedTypeReference<List<CaseTypeActionDto>> typeReference = new ParameterizedTypeReference<>() {};

        HttpEntity httpEntity = new HttpEntity(headers);
        ResponseEntity<List<CaseTypeActionDto>> response = restTemplate.exchange(getBasePath() + "/caseType/actions",
            HttpMethod.GET, httpEntity, typeReference);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Objects.requireNonNull(response.getBody()).size()).isEqualTo(4);

    }

    private String getBasePath() {
        return "http://localhost:" + port;
    }

}
