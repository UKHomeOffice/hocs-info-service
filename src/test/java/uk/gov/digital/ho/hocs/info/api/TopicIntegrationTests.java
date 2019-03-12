package uk.gov.digital.ho.hocs.info.api;

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
import uk.gov.digital.ho.hocs.info.domain.model.ParentTopic;
import uk.gov.digital.ho.hocs.info.domain.model.Topic;
import uk.gov.digital.ho.hocs.info.domain.repository.ParentTopicRepository;
import uk.gov.digital.ho.hocs.info.domain.repository.TopicRepository;

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
    final UUID inactiveParentTopicUUID = UUID.fromString("71caee7b-4632-4ac6-9c15-b91d4c0d27e5");
    final UUID invalidParentTopicUUID = UUID.fromString("a1f227ca-707c-488e-a21f-bdd94f37e5bb");
    final UUID topicUUID = UUID.fromString("11111111-ffff-1111-1111-111111111131");

    @Before
    public void setup(){
        headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.toString());
    }

    @Test
    public void shouldCreateParentTopic(){

        CreateParentTopicDto request = new CreateParentTopicDto("Test Parent Topic");

        long numberOfTopicsBefore = parentTopicRepository.count();

        HttpEntity httpEntity = new HttpEntity(request, headers);
        ResponseEntity<CreateParentTopicResponse> result = restTemplate.exchange(
                getBasePath() + "/topic/parent"
                , HttpMethod.POST, httpEntity, CreateParentTopicResponse.class);

        long numberOfTopicsAfter = parentTopicRepository.count();

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isNotNull();
        assertThat(numberOfTopicsAfter).isEqualTo(numberOfTopicsBefore + 1l);
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

        CreateParentTopicDto request = new CreateParentTopicDto("test inactive parent topic 102");

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

        long numberOfTopicsBefore = topicRepository.findTopicByParentTopic(parentTopicUUID).size();

        CreateTopicDto request = new CreateTopicDto("test topic");

        HttpEntity httpEntity = new HttpEntity(request, headers);
        ResponseEntity<CreateTopicResponse> result = restTemplate.exchange(
                getBasePath() + "/topic/parent/" + parentTopicUUID.toString()
                , HttpMethod.POST, httpEntity, CreateTopicResponse.class);

        long numberOfTopicsAfter = topicRepository.findTopicByParentTopic(parentTopicUUID).size();

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isNotNull();
        assertThat(numberOfTopicsAfter).isEqualTo(numberOfTopicsBefore + 1l);

    }

    @Test
    public void shouldNotCreateTopicWhenGivenParentUUIDDoesNotExist() {

        long numberOfTopicsBefore = topicRepository.count();

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
                getBasePath() + "/topic/parent/" + parentTopicUUID
                , HttpMethod.POST, httpEntity, Void.class);

        long numberOfTopicsAfter = topicRepository.count();

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(numberOfTopicsAfter).isEqualTo(numberOfTopicsBefore);
    }

    @Test
    public void shouldNotCreateTopicWhenParentHasInactiveTopicWithSameNameAsGiven() {

        long numberOfTopicsBefore = topicRepository.count();

        CreateTopicDto request = new CreateTopicDto("test inactive topic 4");

        HttpEntity httpEntity = new HttpEntity(request, headers);
        ResponseEntity<Void> result = restTemplate.exchange(
                getBasePath() + "/topic/parent/" + parentTopicUUID
                , HttpMethod.POST, httpEntity, Void.class);

        long numberOfTopicsAfter = topicRepository.count();

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(numberOfTopicsAfter).isEqualTo(numberOfTopicsBefore);
    }



    @Test
    public void shouldMakeParentTopicWithNoChildrenTopicsInactive() {

        UUID parentTopicWithNoChildrenUUID = UUID.fromString("038cecb8-00a2-4417-b18b-88c8905ff52e");

        HttpEntity httpEntity = new HttpEntity(headers);

        ResponseEntity<Void> result = restTemplate.exchange(
                getBasePath() + "/topic/parent/" + parentTopicWithNoChildrenUUID
                , HttpMethod.DELETE, httpEntity, Void.class);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(parentTopicRepository.findByUuid(parentTopicWithNoChildrenUUID).getActive()).isFalse();

    }

    @Test
    public void shouldMakeParentTopicAndItsChildrenInactive(){

        UUID topicUUID1 = UUID.fromString("11111111-ffff-1111-1111-111111111131");
        UUID topicUUID2 = UUID.fromString("11111111-ffff-1111-1111-111111111132");

        HttpEntity httpEntity = new HttpEntity(headers);

        ResponseEntity<Void> result = restTemplate.exchange(
                getBasePath() + "/topic/parent/" + parentTopicUUID
                , HttpMethod.DELETE, httpEntity, Void.class);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(parentTopicRepository.findByUuid(parentTopicUUID).getActive()).isFalse();
        assertThat(topicRepository.findTopicByUUID(topicUUID1).getActive()).isFalse();
        assertThat(topicRepository.findTopicByUUID(topicUUID2).getActive()).isFalse();

    }

    @Test
    public void shouldMakeChildTopicInactiveButNotParentWithValidActiveParent() {

        HttpEntity httpEntity = new HttpEntity(headers);

        ResponseEntity<Void> result = restTemplate.exchange(
                getBasePath() + "/topic/" + topicUUID
                , HttpMethod.DELETE, httpEntity, Void.class);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(parentTopicRepository.findByUuid(parentTopicUUID).getActive()).isTrue();
        assertThat(topicRepository.findTopicByUUID(topicUUID).getActive()).isFalse();
    }

    @Test
    public void shouldNotMakeChildOrParentTopicsInactiveWhenGivenNonexistentParentUuid() {

        long activeTopicsBefore = topicRepository.findAllByActiveIsTrue().size();
        long activeParentTopicsBefore = parentTopicRepository.findAllByActiveIsTrue().size();

        HttpEntity httpEntity = new HttpEntity(headers);

        ResponseEntity result = restTemplate.exchange(
                getBasePath() + "/topic/parent/" + invalidParentTopicUUID
                , HttpMethod.DELETE, httpEntity, Void.class);

        long activeTopicsAfter= topicRepository.findAllByActiveIsTrue().size();
        long activeParentTopicsAfter = parentTopicRepository.findAllByActiveIsTrue().size();

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(activeTopicsAfter).isEqualTo(activeTopicsBefore);
        assertThat(activeParentTopicsAfter).isEqualTo(activeParentTopicsBefore);

    }

    @Test
    public void shouldReactivateParentTopicButNotChildTopic() {

        String childTopicUUID = "11111111-ffff-1111-1111-111111111135";
        HttpEntity httpEntity = new HttpEntity(headers);

        ResponseEntity result = restTemplate.exchange(
                getBasePath() + "/topic/parent/" + inactiveParentTopicUUID
                , HttpMethod.PUT, httpEntity, Void.class);

        ParentTopic parentTopic = parentTopicRepository.findByUuid(inactiveParentTopicUUID);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(parentTopic.getActive()).isEqualTo(true);

        Topic topic = topicRepository.findTopicByUUID(UUID.fromString(childTopicUUID));
        assertThat(topic.getActive()).isEqualTo(false);
        assertThat(parentTopic.getActive()).isEqualTo(true);

    }

    @Test
    public void shouldNotReactivateParentTopicWhenGivenNonexistentUuid() {

        long activeParentTopicsBefore = parentTopicRepository.findAllByActiveIsTrue().size();

        HttpEntity httpEntity = new HttpEntity(headers);
        ResponseEntity<Void> result = restTemplate.exchange(
                getBasePath() + "/topic/parent/" + invalidParentTopicUUID
                , HttpMethod.PUT, httpEntity, Void.class);

        long activeParentTopicsAfter = parentTopicRepository.findAllByActiveIsTrue().size();

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(activeParentTopicsAfter).isEqualTo(activeParentTopicsBefore);

    }

    @Test
    public void shouldNotChangeActiveOfParentTopicWhenAlreadyActive() {

        ParentTopic parentTopicBefore = parentTopicRepository.findByUuid(parentTopicUUID);

        HttpEntity httpEntity = new HttpEntity(headers);
        ResponseEntity result = restTemplate.exchange(
                getBasePath() + "/topic/parent/" + parentTopicUUID
                , HttpMethod.PUT, httpEntity, Void.class);

        ParentTopic parentTopicAfter = parentTopicRepository.findByUuid(parentTopicUUID);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(parentTopicBefore.getActive()).isEqualTo(parentTopicAfter.getActive());
        assertThat(parentTopicBefore.getDisplayName()).isEqualTo(parentTopicAfter.getDisplayName());

    }

    @Test
    public void shouldReactivateInactiveTopicWithActiveParent() {

        UUID inactiveTopicUUID = UUID.fromString("11111111-ffff-1111-1111-111111111134");

        HttpEntity httpEntity = new HttpEntity(headers);
        ResponseEntity result = restTemplate.exchange(
                getBasePath() + "/topic/" + inactiveTopicUUID
                , HttpMethod.PUT, httpEntity, Void.class);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(topicRepository.findTopicByUUID(inactiveTopicUUID).getActive()).isEqualTo(true);

    }

    @Test
    public void shouldNotReactivateTopicWhenItsParentIsInactive() {

        UUID inactiveTopicWithInactiveParentUUID = UUID.fromString("11111111-ffff-1111-1111-111111111135");

        HttpEntity httpEntity = new HttpEntity(headers);
        ResponseEntity result = restTemplate.exchange(
                getBasePath() + "/topic/" + inactiveTopicWithInactiveParentUUID
                , HttpMethod.PUT, httpEntity, Void.class);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(topicRepository.findTopicByUUID(inactiveTopicWithInactiveParentUUID).getActive()).isEqualTo(false);

    }

    @Test
    public void shouldNotChangeActiveOfTopicWhenAlreadyActive() {

        Topic topicBefore = topicRepository.findTopicByUUID(topicUUID);

        HttpEntity httpEntity = new HttpEntity(headers);
        ResponseEntity result = restTemplate.exchange(
                getBasePath() + "/topic/" + topicUUID
                , HttpMethod.PUT, httpEntity, Void.class);

        Topic topicAfter = topicRepository.findTopicByUUID(topicUUID);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(topicBefore.getActive()).isEqualTo(topicAfter.getActive());

    }

    @Test
    public void shouldUpdateParentTopicOfGivenActiveTopic(){

        UUID newParentTopicUUID = UUID.fromString("1abf7a0c-ea2d-478d-b6c8-d739fb60ef04");
        UpdateTopicParentDto request = new UpdateTopicParentDto(newParentTopicUUID.toString());

        HttpEntity httpEntity = new HttpEntity(request, headers);
        ResponseEntity result = restTemplate.exchange(
                getBasePath() + "/topic/" + topicUUID + "/parent"
                , HttpMethod.PUT, httpEntity, Void.class);

        Topic topic = topicRepository.findTopicByUUID(topicUUID);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(topic.getParentTopic()).isEqualTo(newParentTopicUUID);

    }

    @Test
    public void shouldNotUpdateParentOfTopicWithGivenInactiveParentTopic(){

        UpdateTopicParentDto request = new UpdateTopicParentDto(inactiveParentTopicUUID.toString());

        HttpEntity httpEntity = new HttpEntity(request, headers);
        ResponseEntity result = restTemplate.exchange(
                getBasePath() + "/topic/" + topicUUID + "/parent"
                , HttpMethod.PUT, httpEntity, Void.class);

        Topic topic = topicRepository.findTopicByUUID(topicUUID);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(topic.getParentTopic()).isNotEqualTo(inactiveParentTopicUUID);

    }

    @Test
    public void shouldNotUpdateParentOfTopicWithGivenInvalidParentTopic(){

        UpdateTopicParentDto request = new UpdateTopicParentDto(invalidParentTopicUUID.toString());

        HttpEntity httpEntity = new HttpEntity(request, headers);
        ResponseEntity result = restTemplate.exchange(
                getBasePath() + "/topic/" + topicUUID + "/parent"
                , HttpMethod.PUT, httpEntity, Void.class);

        Topic topic = topicRepository.findTopicByUUID(topicUUID);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(topic.getParentTopic()).isNotEqualTo(invalidParentTopicUUID);

    }

    @Test
    public void shouldUpdateDisplayNameOfGivenTopic(){

        UpdateTopicNameDto request = new UpdateTopicNameDto("New name");

        HttpEntity httpEntity = new HttpEntity(request, headers);
        ResponseEntity result = restTemplate.exchange(
                getBasePath() + "/topic/" + topicUUID + "/name"
                , HttpMethod.PUT, httpEntity, Void.class);

        Topic topic = topicRepository.findTopicByUUID(topicUUID);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(topic.getDisplayName()).isEqualTo("New name");

    }

    private String getBasePath() {
        return "http://localhost:" + port;
    }

}