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
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import uk.gov.digital.ho.hocs.info.api.dto.CorrespondentTypeDto;
import uk.gov.digital.ho.hocs.info.api.dto.CreateCorrespondentTypeDto;
import uk.gov.digital.ho.hocs.info.api.dto.CreateCorrespondentTypeResponse;
import uk.gov.digital.ho.hocs.info.api.dto.GetCorrespondentTypesResponse;
import uk.gov.digital.ho.hocs.info.domain.model.CorrespondentType;
import uk.gov.digital.ho.hocs.info.domain.repository.CorrespondentTypeRepository;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.SqlConfig.TransactionMode.ISOLATED;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "classpath:beforeTest.sql", config = @SqlConfig(transactionMode = ISOLATED))
@Sql(scripts = "classpath:afterTest.sql", config = @SqlConfig(transactionMode = ISOLATED), executionPhase = AFTER_TEST_METHOD)
@ActiveProfiles("test")
public class CorrespondentTypeIntegrationTests {

    TestRestTemplate restTemplate = new TestRestTemplate();

    @Autowired
    CorrespondentTypeRepository correspondentTypeRepository;

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
    public void shouldGetAllCorrespondentTypes() {

        long correspondentCount = correspondentTypeRepository.findAll().size();

        HttpEntity httpEntity = new HttpEntity(headers);
        ResponseEntity<GetCorrespondentTypesResponse> result = restTemplate.exchange(
                getBasePath() + "/correspondentType"
                , HttpMethod.GET, httpEntity, GetCorrespondentTypesResponse.class);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody().getCorrespondentTypes().size()).isEqualTo(correspondentCount);

    }

    public void shouldAddCorrespondentType() {
        CreateCorrespondentTypeDto request = new CreateCorrespondentTypeDto("Member", "MEMBER");

        HttpEntity<CreateCorrespondentTypeDto> httpEntity = new HttpEntity(request, headers);
        ResponseEntity<CreateCorrespondentTypeResponse> result = restTemplate.exchange(
                getBasePath() + "/correspondentType/"
                , HttpMethod.POST, httpEntity, CreateCorrespondentTypeResponse.class );

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody().getDisplayName()).isEqualTo("Member");
        assertThat(result.getBody().getType()).isEqualTo("MEMBER");
    }

    private String getBasePath() {
        return "http://localhost:" + port;
    }

}