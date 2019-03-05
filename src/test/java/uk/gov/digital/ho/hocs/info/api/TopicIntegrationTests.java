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
import org.springframework.transaction.annotation.Transactional;
import uk.gov.digital.ho.hocs.info.api.dto.*;
import uk.gov.digital.ho.hocs.info.domain.model.ParentTopic;
import uk.gov.digital.ho.hocs.info.domain.model.Topic;
import uk.gov.digital.ho.hocs.info.domain.repository.TeamRepository;
import uk.gov.digital.ho.hocs.info.domain.repository.TopicRepository;
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
public class TopicIntegrationTests {

    TestRestTemplate restTemplate = new TestRestTemplate();

    @Autowired
    TopicRepository topicRepository;

    private HttpHeaders headers;

    @LocalServerPort
    int port;

    @Autowired
    ObjectMapper mapper;

    final UUID parentTopicUUID = UUID.fromString("94a10f9f-a42e-44c0-8ebe-1227cb347f1d");


    @Before
    public void setup() throws IOException {
        headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.toString());
    }

//    @Test
//    public void shouldMakeTopicWithNoChildrenTopicsInactive() {
//
//        String topicUUID = "11111111-ffff-1111-1111-111111111131";
//        HttpEntity httpEntity = new HttpEntity(headers);
//        ResponseEntity<String> result = restTemplate.exchange(
//                getBasePath() + "/topic/" + topicUUID
//                , HttpMethod.DELETE, httpEntity, String.class);
//
//        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
//        assertThat(topicRepository.findTopicByUUID(UUID.fromString(topicUUID)).isActive()).isFalse();
//
//    }
//
//    @Test
//    public void shouldMakeParentTopicAndItsChildrenInactive() throws JsonProcessingException {
//
//        String parentTopicUUID = "94a10f9f-a42e-44c0-8ebe-1227cb347f1d";
//        String topicUUID = "11111111-ffff-1111-1111-111111111131";
//
//        String body = createBody();
//        HttpEntity httpEntity = new HttpEntity(headers);
//        ResponseEntity<String> result = restTemplate.exchange(
//                getBasePath() + "/topic/" + parentTopicUUID
//                , HttpMethod.DELETE, httpEntity, String.class);
//
//        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
//        assertThat(topicRepository.findTopicByUUID(UUID.fromString(parentTopicUUID)).isActive()).isFalse();
//        assertThat(topicRepository.findTopicByUUID(UUID.fromString(topicUUID)).isActive()).isFalse();
//    }

    @Test
    public void shouldCreateParentTopic() throws JsonProcessingException {

        CreateParentTopicDto request = new CreateParentTopicDto("Parent topic 1");
        String body = mapper.writeValueAsString(request);

        HttpEntity httpEntity = new HttpEntity(body, headers);
        ResponseEntity<UUID> result = restTemplate.exchange(
                getBasePath() + "/topic/parent/"
                , HttpMethod.POST, httpEntity, UUID.class);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isNotNull();
    }


    @Test
    public void shouldNotCreateParentTopicWhenActiveParentTopicExistsWithGivenName() throws JsonProcessingException {

        CreateParentTopicDto request = new CreateParentTopicDto("test Parent topic 100");
        String body = mapper.writeValueAsString(request);

        HttpEntity httpEntity = new HttpEntity(body, headers);
        ResponseEntity<UUID> result = restTemplate.exchange(
                getBasePath() + "/topic/parent/"
                , HttpMethod.POST, httpEntity, UUID.class);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(result.getBody()).isNull();
    }


    @Test
    public void shouldNotCreateParentTopicWhenInactiveParentTopicExistsWithGivenName() throws JsonProcessingException {

        CreateParentTopicDto request = new CreateParentTopicDto("test inactive Parent topic 102");
        String body = mapper.writeValueAsString(request);

        HttpEntity httpEntity = new HttpEntity(body, headers);
        ResponseEntity<UUID> result = restTemplate.exchange(
                getBasePath() + "/topic/parent/"
                , HttpMethod.POST, httpEntity, UUID.class);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(result.getBody()).isNull();
    }
    @Test
    public void shouldCreateTopicWithValidParent() throws JsonProcessingException {

        CreateTopicDto request = new CreateTopicDto("Topic 1");
        String body = mapper.writeValueAsString(request);

        HttpEntity httpEntity = new HttpEntity(body, headers);
        ResponseEntity<UUID> result = restTemplate.exchange(
                getBasePath() + "/topic/" + parentTopicUUID.toString()
                , HttpMethod.POST, httpEntity, UUID.class);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isNotNull();
    }

    @Test
    public void shouldNotCreateTopicWhenGivenParentUUIDDoesNotExist() throws JsonProcessingException {

        String invalidParentTopicUUID = "a1f227ca-707c-488e-a21f-bdd94f37e5bb";
        CreateTopicDto request = new CreateTopicDto("Topic 1");
        String body = mapper.writeValueAsString(request);

        HttpEntity httpEntity = new HttpEntity(body, headers);
        ResponseEntity<UUID> result = restTemplate.exchange(
                getBasePath() + "/topic/" + invalidParentTopicUUID
                , HttpMethod.POST, httpEntity, UUID.class);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(result.getBody()).isNull();
    }

    @Test
    public void shouldNotCreateChildTopicWhenGivenInactiveParent() throws JsonProcessingException {

        String inactiveParentTopicUUID = "71caee7b-4632-4ac6-9c15-b91d4c0d27e5";
        CreateTopicDto request = new CreateTopicDto("Topic 1");
        String body = mapper.writeValueAsString(request);

        HttpEntity httpEntity = new HttpEntity(body, headers);
        ResponseEntity<UUID> result = restTemplate.exchange(
                getBasePath() + "/topic/" + inactiveParentTopicUUID
                , HttpMethod.POST, httpEntity, UUID.class);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(result.getBody()).isNull();
    }

    @Test
    public void shouldNotCreateTopicWhenParentHasActiveTopicWithSameNameAsGiven() throws JsonProcessingException {

        CreateTopicDto request = new CreateTopicDto("test topic 1");
        String body = mapper.writeValueAsString(request);

        HttpEntity httpEntity = new HttpEntity(body, headers);
        ResponseEntity<UUID> result = restTemplate.exchange(
                getBasePath() + "/topic/" + parentTopicUUID.toString()
                , HttpMethod.POST, httpEntity, UUID.class);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(result.getBody()).isNull();
    }

    @Test
    public void shouldNotCreateTopicWhenParentHasInactiveTopicWithSameNameAsGiven() throws JsonProcessingException {

        CreateTopicDto request = new CreateTopicDto("test inactive topic 3");
        String body = mapper.writeValueAsString(request);

        HttpEntity httpEntity = new HttpEntity(body, headers);
        ResponseEntity<UUID> result = restTemplate.exchange(
                getBasePath() + "/topic/" + parentTopicUUID.toString()
                , HttpMethod.POST, httpEntity, UUID.class);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(result.getBody()).isNull();
    }

    private String getBasePath() {
        return "http://localhost:" + port;
    }

}


