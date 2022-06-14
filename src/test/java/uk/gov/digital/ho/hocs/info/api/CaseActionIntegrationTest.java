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
import org.springframework.test.annotation.DirtiesContext;
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
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.SqlConfig.TransactionMode.ISOLATED;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "classpath:beforeTest.sql", config = @SqlConfig(transactionMode = ISOLATED))
@Sql(scripts = "classpath:afterTest.sql", config = @SqlConfig(transactionMode = ISOLATED), executionPhase = AFTER_TEST_METHOD)
@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
@ActiveProfiles({"local", "integration"})
public class CaseActionIntegrationTest {

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
                getBasePath() + "/caseType?initialCaseType=true", HttpMethod.GET, httpEntity, new ParameterizedTypeReference<>() {
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
    public void shouldRetrieveAllCaseTypes() {
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
    public void shouldReturnRequestedActionById() {
        String actionID = "f2b625c9-7250-4293-9e68-c8f515e3043d";

        ParameterizedTypeReference<CaseTypeActionDto> typeReference = new ParameterizedTypeReference<>() {};

        HttpEntity httpEntity = new HttpEntity(headers);
        ResponseEntity<CaseTypeActionDto> response = restTemplate.exchange(
                getBasePath() + "/actions/" + actionID,
                HttpMethod.GET,
                httpEntity,
                typeReference
        );

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("APPEAL 1", Objects.requireNonNull(response.getBody()).getActionLabel());
    }


    @Test
    public void shouldReturnRequestedActionLabelById() {
        String actionID = "f2b625c9-7250-4293-9e68-c8f515e3043d";

        HttpEntity httpEntity = new HttpEntity(headers);
        ResponseEntity<String> response = restTemplate.exchange(
                getBasePath() + "/actions/" + actionID + "/label",
                HttpMethod.GET,
                httpEntity,
                String.class
        );

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("APPEAL 1", Objects.requireNonNull(response.getBody()));

    }

    private String getBasePath() {
        return "http://localhost:" + port;
    }

}
