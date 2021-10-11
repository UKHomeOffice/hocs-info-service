package uk.gov.digital.ho.hocs.info.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.entity.ContentType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import uk.gov.digital.ho.hocs.info.api.dto.CaseActionTypeDto;

import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.SqlConfig.TransactionMode.ISOLATED;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "classpath:beforeTest.sql", config = @SqlConfig(transactionMode = ISOLATED))
@Sql(scripts = "classpath:afterTest.sql", config = @SqlConfig(transactionMode = ISOLATED), executionPhase = AFTER_TEST_METHOD)
@ActiveProfiles("test")
public class CaseActionIntegrationTest {

    TestRestTemplate restTemplate = new TestRestTemplate();

    private HttpHeaders headers;

    @LocalServerPort
    int port;

    @Autowired
    ObjectMapper mapper;

    @Before
    public void setup() {
        headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.toString());
    }

    @Test
    public void shouldGetAllCaseActionsForCaseType() {

        String caseTypeString = "CT1";

        ParameterizedTypeReference<List<CaseActionTypeDto>> typeReference = new ParameterizedTypeReference<>() {};

        HttpEntity httpEntity = new HttpEntity(headers);
        ResponseEntity<List<CaseActionTypeDto>> response = restTemplate.exchange(
                getBasePath() + "/case-actions/" + caseTypeString,
                HttpMethod.GET,
                httpEntity,
                typeReference
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Objects.requireNonNull(response.getBody()).size()).isEqualTo(2);
    }

    @Test
    public void shouldReturnEmptyArrayWhenNoActionsForCaseType() {

        String caseTypeString = "CASE_NON_EXISTENT";

        ParameterizedTypeReference<List<CaseActionTypeDto>> typeReference = new ParameterizedTypeReference<>() {};

        HttpEntity httpEntity = new HttpEntity(headers);
        ResponseEntity<List<CaseActionTypeDto>> response = restTemplate.exchange(
                getBasePath() + "/case-actions/" + caseTypeString,
                HttpMethod.GET,
                httpEntity,
                typeReference
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Objects.requireNonNull(response.getBody()).size()).isEqualTo(0);
    }

    private String getBasePath() {
        return "http://localhost:" + port;
    }
}
