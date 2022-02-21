package uk.gov.digital.ho.hocs.info.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.entity.ContentType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import uk.gov.digital.ho.hocs.info.api.dto.SchemaDto;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.SqlConfig.TransactionMode.ISOLATED;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = { "user.email.whitelist=homeoffice.gov.uk" },
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "classpath:beforeTest.sql", config = @SqlConfig(transactionMode = ISOLATED))
@Sql(scripts = "classpath:afterTest.sql", config = @SqlConfig(transactionMode = ISOLATED), executionPhase = AFTER_TEST_METHOD)
@ActiveProfiles({"local", "integration"})
public class SchemaIntegrationTest {

    public static final String TEST_SCREEN_SCHEMA = "TEST_SCREEN_SCHEMA";

    private TestRestTemplate restTemplate;

    @Autowired
    RestTemplateBuilder restTemplateBuilder;

    private HttpHeaders headers;

    @LocalServerPort
    int port;

    @Autowired
    ObjectMapper mapper;

    @Before
    public void setup() throws IOException {
        headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.toString());

        restTemplate = new TestRestTemplate(restTemplateBuilder.rootUri("http://localhost:" + port));
    }

    @Test
    public void shouldGetSchema() {
        String schemaType = TEST_SCREEN_SCHEMA;

        HttpEntity<SchemaDto> httpEntity = new HttpEntity<>(headers);

        ResponseEntity<HashMap> result = restTemplate.exchange(
                "/schema/" + schemaType
                , HttpMethod.GET, httpEntity, HashMap.class);


        HashMap<String, Object> body = result.getBody();
        assertThat(body.get("uuid")).isEqualTo("f958f77d-b277-408d-bd6f-4a498d3f217f");
        assertThat(((List) body.get("fields")).size()).isEqualTo(2);
    }
}
