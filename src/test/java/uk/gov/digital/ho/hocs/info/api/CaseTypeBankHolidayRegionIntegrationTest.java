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
import uk.gov.digital.ho.hocs.info.domain.repository.CorrespondentTypeRepository;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.SqlConfig.TransactionMode.ISOLATED;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "classpath:beforeTest.sql", config = @SqlConfig(transactionMode = ISOLATED))
@Sql(scripts = "classpath:afterTest.sql", config = @SqlConfig(transactionMode = ISOLATED), executionPhase = AFTER_TEST_METHOD)
@ActiveProfiles({"local", "integration"})
public class CaseTypeBankHolidayRegionIntegrationTest {

    TestRestTemplate restTemplate = new TestRestTemplate();

    @Autowired
    CorrespondentTypeRepository correspondentTypeRepository;

    private HttpHeaders headers;

    @LocalServerPort
    int port;

    @Autowired
    ObjectMapper mapper;

    private final UUID caseTypeUuid = UUID.fromString("bf429d0e-b106-4644-83c6-3b15f2e5f749");

    @Before
    public void setup() {
        headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.toString());
    }

    @Test
    public void shouldGetAllBankHolidayDates() {
        HttpEntity httpEntity = new HttpEntity(headers);
        ResponseEntity<List<String>> result = restTemplate.exchange(
                getBasePath() + "/bankHolidayRegion/caseTypeUuid/" + caseTypeUuid
                , HttpMethod.GET, httpEntity, new ParameterizedTypeReference<>() {});

        final List<String> resultBody = result.getBody();

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(resultBody.size()).isEqualTo(2);

        assertThat(resultBody.stream().filter(value -> value.equals("ENGLAND_AND_WALES")).count()).isEqualTo(1);
        assertThat(resultBody.stream().filter(value -> value.equals("SCOTLAND")).count()).isEqualTo(1);
    }

    private String getBasePath() {
        return "http://localhost:" + port;
    }
}
