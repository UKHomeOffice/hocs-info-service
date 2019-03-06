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
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import uk.gov.digital.ho.hocs.info.api.dto.*;
import uk.gov.digital.ho.hocs.info.domain.repository.ParentTopicRepository;
import uk.gov.digital.ho.hocs.info.domain.repository.TopicRepository;

import java.io.IOException;
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

    @Autowired
    ParentTopicRepository parentTopicRepository;

    private HttpHeaders headers;

    @LocalServerPort
    int port;

    final UUID parentTopicUUID = UUID.fromString("94a10f9f-a42e-44c0-8ebe-1227cb347f1d");


    @Before
    public void setup(){
        headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.toString());
    }


    @Test
    public void shouldCreateParentTopic(){

        CreateParentTopicDto request = new CreateParentTopicDto("Test Parent Topic");
        HttpEntity httpEntity = new HttpEntity(request, headers);

        ResponseEntity<CreateParentTopicResponse> result = restTemplate.exchange(
                getBasePath() + "/topic/parent"
                , HttpMethod.POST, httpEntity, CreateParentTopicResponse.class);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isNotNull();
    }

    @Test
    public void shouldNotCreateParentTopicWhenActiveParentTopicExistsWithGivenName() {

        long numberOfTopicsBefore = parentTopicRepository.count();

        CreateParentTopicDto request = new CreateParentTopicDto("test Parent topic 100");
        HttpEntity httpEntity = new HttpEntity(request, headers);

        ResponseEntity<Void> result = restTemplate.exchange(
                getBasePath() + "/topic/parent/"
                , HttpMethod.POST, httpEntity, Void.class);

        long numberOfTopicsAfter = parentTopicRepository.count();

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(numberOfTopicsAfter).isEqualTo(numberOfTopicsBefore);
    }

    @Test
    public void shouldNotCreateParentTopicWhenInactiveParentTopicExistsWithGivenName() {

        long numberOfTopicsBefore = parentTopicRepository.count();

        CreateParentTopicDto request = new CreateParentTopicDto("test inactive Parent topic 102");

        HttpEntity httpEntity = new HttpEntity(request, headers);
        ResponseEntity<Void> result = restTemplate.exchange(
                getBasePath() + "/topic/parent/"
                , HttpMethod.POST, httpEntity, Void.class);

        long numberOfTopicsAfter = parentTopicRepository.count();


        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(numberOfTopicsAfter).isEqualTo(numberOfTopicsBefore);
    }

    @Test
    public void shouldCreateTopicWithValidParent() {

        CreateTopicDto request = new CreateTopicDto("Test topic");

        HttpEntity httpEntity = new HttpEntity(request, headers);
        ResponseEntity<CreateTopicResponse> result = restTemplate.exchange(
                getBasePath() + "/topic/parent/" + parentTopicUUID.toString()
                , HttpMethod.POST, httpEntity, CreateTopicResponse.class);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isNotNull();
    }

    @Test
    public void shouldNotCreateTopicWhenGivenParentUUIDDoesNotExist() {

        long numberOfTopicsBefore = topicRepository.count();

        String invalidParentTopicUUID = "a1f227ca-707c-488e-a21f-bdd94f37e5bb";
        CreateTopicDto request = new CreateTopicDto("Topic 1");

        HttpEntity httpEntity = new HttpEntity(request, headers);
        ResponseEntity<Void> result = restTemplate.exchange(
                getBasePath() + "/topic/parent/" + invalidParentTopicUUID
                , HttpMethod.POST, httpEntity, Void.class);

        long numberOfTopicsAfter = topicRepository.count();


        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(numberOfTopicsAfter).isEqualTo(numberOfTopicsBefore);
    }

    @Test
    public void shouldNotCreateChildTopicWhenGivenInactiveParent() {

        long numberOfTopicsBefore = topicRepository.count();

        String inactiveParentTopicUUID = "71caee7b-4632-4ac6-9c15-b91d4c0d27e5";
        CreateTopicDto request = new CreateTopicDto("Topic 1");

        HttpEntity httpEntity = new HttpEntity(request, headers);
        ResponseEntity<Void> result = restTemplate.exchange(
                getBasePath() + "/topic/parent/" + inactiveParentTopicUUID
                , HttpMethod.POST, httpEntity, Void.class);

        long numberOfTopicsAfter = topicRepository.count();


        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(numberOfTopicsAfter).isEqualTo(numberOfTopicsBefore);
    }

    @Test
    public void shouldNotCreateTopicWhenParentHasActiveTopicWithSameNameAsGiven() {

        long numberOfTopicsBefore = topicRepository.count();

        CreateTopicDto request = new CreateTopicDto("test topic 1");

        HttpEntity httpEntity = new HttpEntity(request, headers);
        ResponseEntity<Void> result = restTemplate.exchange(
                getBasePath() + "/topic/parent/" + parentTopicUUID.toString()
                , HttpMethod.POST, httpEntity, Void.class);

        long numberOfTopicsAfter = topicRepository.count();


        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(numberOfTopicsAfter).isEqualTo(numberOfTopicsBefore);
    }

    @Test
    public void shouldNotCreateTopicWhenParentHasInactiveTopicWithSameNameAsGiven() {

        long numberOfTopicsBefore = topicRepository.count();

        CreateTopicDto request = new CreateTopicDto("test inactive topic 3");

        HttpEntity httpEntity = new HttpEntity(request, headers);
        ResponseEntity<Void> result = restTemplate.exchange(
                getBasePath() + "/topic/parent/" + parentTopicUUID.toString()
                , HttpMethod.POST, httpEntity, Void.class);

        long numberOfTopicsAfter = topicRepository.count();

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(numberOfTopicsAfter).isEqualTo(numberOfTopicsBefore);
    }

    private String getBasePath() {
        return "http://localhost:" + port;
    }



    @Test
    public void shouldMakeParentTopicWithNoChildrenTopicsInactive() {

        String topicUUID = "11111111-ffff-1111-1111-111111111131";

        HttpEntity httpEntity = new HttpEntity(headers);

        ResponseEntity<Void> result = restTemplate.exchange(
                getBasePath() + "/topic/" + topicUUID
                , HttpMethod.DELETE, httpEntity, Void.class);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(topicRepository.findTopicByUUID(UUID.fromString(topicUUID)).getActive()).isFalse();

    }

    @Test
    public void shouldMakeParentTopicAndItsChildrenInactive(){

        String parentTopicUUID = "94a10f9f-a42e-44c0-8ebe-1227cb347f1d";
        String topicUUID1 = "11111111-ffff-1111-1111-111111111131";
        String topicUUID2 = "11111111-ffff-1111-1111-111111111134";

        HttpEntity httpEntity = new HttpEntity(headers);

        ResponseEntity<Void> result = restTemplate.exchange(
                getBasePath() + "/topic/parent/" + parentTopicUUID
                , HttpMethod.DELETE, httpEntity, Void.class);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(parentTopicRepository.findByUuid(UUID.fromString(parentTopicUUID)).getActive()).isFalse();
        assertThat(topicRepository.findTopicByUUID(UUID.fromString(topicUUID1)).getActive()).isFalse();
        assertThat(topicRepository.findTopicByUUID(UUID.fromString(topicUUID2)).getActive()).isFalse();

    }

    @Test
    public void shouldMakeChildTopicInactiveWithValidActiveParent() {

        String parentTopicUUID = "94a10f9f-a42e-44c0-8ebe-1227cb347f1d";
        String topicUUID = "11111111-ffff-1111-1111-111111111131";

        HttpEntity httpEntity = new HttpEntity(headers);

        ResponseEntity<Void> result = restTemplate.exchange(
                getBasePath() + "/topic/" + topicUUID
                , HttpMethod.DELETE, httpEntity, Void.class);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(parentTopicRepository.findByUuid(UUID.fromString(parentTopicUUID)).getActive()).isTrue();
        assertThat(topicRepository.findTopicByUUID(UUID.fromString(topicUUID)).getActive()).isFalse();
    }

    @Test
    public void shouldNotMakeChildTopicInactiveWithInvalidParentTopic() {

        String parentTopicUUID = "94a10f9f-a42e-44c0-8ebe-1227cxxxxxxx";
        String topicUUID = "11111111-ffff-1111-1111-111111111131";

        HttpEntity httpEntity = new HttpEntity(headers);

        ResponseEntity<Void> result = restTemplate.exchange(
                getBasePath() + "/topic/" + parentTopicUUID
                , HttpMethod.DELETE, httpEntity, Void.class);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(topicRepository.findTopicByUUID(UUID.fromString(topicUUID)).getActive()).isTrue();
    }
}