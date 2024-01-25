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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.SqlConfig.TransactionMode.ISOLATED;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "classpath:beforeTest.sql", config = @SqlConfig(transactionMode = ISOLATED))
@Sql(scripts = "classpath:afterTest.sql",
     config = @SqlConfig(transactionMode = ISOLATED),
     executionPhase = AFTER_TEST_METHOD)
@ActiveProfiles({ "local", "integration" })
public class EntityIntegrationTest {

    TestRestTemplate restTemplate = new TestRestTemplate();

    @LocalServerPort
    int port;

    @Autowired
    ObjectMapper mapper;

    private HttpHeaders headers;

    @Before
    public void setup() throws IOException {
        headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.toString());
    }

    @Test
    public void shouldRetrieveEntityBySimpleName() {

        // given
        // setup done in before.sql
        HttpEntity httpEntity = new HttpEntity(headers);

        // when
        ResponseEntity<TestEntityDto> entityResponse = restTemplate.exchange(
            getBasePath() + "/entity/simpleName/TEST_ENTITY_2", HttpMethod.GET, httpEntity, TestEntityDto.class);

        // then
        assertThat(entityResponse).isNotNull();
        assertThat(entityResponse.getStatusCodeValue()).isEqualTo(200);

        TestEntityDto entityDto = entityResponse.getBody();
        assertThat(entityDto).isNotNull();
        assertThat(entityDto.getSimpleName()).isEqualTo("TEST_ENTITY_2");
        assertThat(entityDto.getUuid()).isEqualTo("8761bda1-8e26-4189-9133-aab3651aa584");
        assertThat(entityDto.getData().get("title")).isEqualTo("Two");
    }

    @Test
    public void shouldReturn404WhenWhenActionIdNotExist() {
        ResponseEntity<Void> response = restTemplate.exchange(getBasePath() + "/entity/simpleName/NON_EXISTANT_ENTITY",
            HttpMethod.GET, new HttpEntity<>(headers), Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

    }

    private String getBasePath() {
        return "http://localhost:" + port;
    }

    public static class TestEntityDto {

        private String simpleName;

        private String uuid;

        private HashMap<String, String> data;

        public String getSimpleName() {
            return simpleName;
        }

        public void setSimpleName(String simpleName) {
            this.simpleName = simpleName;
        }

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }

        public HashMap<String, String> getData() {
            return data;
        }

        public void setData(HashMap<String, String> data) {
            this.data = data;
        }

    }

}
