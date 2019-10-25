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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import uk.gov.digital.ho.hocs.info.api.dto.*;
import uk.gov.digital.ho.hocs.info.domain.model.TeamLink;
import uk.gov.digital.ho.hocs.info.domain.repository.TeamLinkRepository;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.SqlConfig.TransactionMode.ISOLATED;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "classpath:beforeTest.sql", config = @SqlConfig(transactionMode = ISOLATED))
@Sql(scripts = "classpath:afterTest.sql", config = @SqlConfig(transactionMode = ISOLATED), executionPhase = AFTER_TEST_METHOD)
@ActiveProfiles("test")
public class TeamLinkIntegrationTests {

    TestRestTemplate restTemplate = new TestRestTemplate();

    @Autowired
    TeamLinkRepository teamLinkRepository;


    private HttpHeaders headers;

    @LocalServerPort
    int port;

    @Before
    public void setup(){
        headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.toString());
    }

    @Test
    public void shouldAddTeamToTopic(){

        UUID topicUUID = UUID.fromString("11111111-ffff-1111-1111-111111111133");
        UUID teamUUID = UUID.fromString("8b3b4366-a37c-48b6-b274-4c50f8083843");
        AddTeamToTopicDto request = new AddTeamToTopicDto("CT1","ST2");

        long numberOfTopicsBefore = teamLinkRepository.findAllByLinkUUIDAndLinkType(topicUUID, "TOPIC").size();

        HttpEntity httpEntity = new HttpEntity(request, headers);
        ResponseEntity result = restTemplate.exchange(
                getBasePath() + "/topic/" + topicUUID + "/team/" + teamUUID
                , HttpMethod.POST, httpEntity, Void.class);

        long numberOfTopicsAfter = teamLinkRepository.findAllByLinkUUIDAndLinkType(topicUUID, "TOPIC").size();

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(numberOfTopicsAfter).isEqualTo(numberOfTopicsBefore + 1l);
        assertThat(teamLinkRepository.findByLinkUUIDAndLinkTypeAndCaseTypeAndStageType(topicUUID, "TOPIC", "CT1", "ST2")).isNotNull();
    }

    @Test
    public void shouldErrorAndNotAddTeamWhenAddingNonExistentTeamToTopic(){

        UUID topicUUID = UUID.fromString("11111111-ffff-1111-1111-111111111133");
        UUID teamUUID = UUID.fromString("8b3b4366-a37c-48b6-b274-4c50f808111A");
        AddTeamToTopicDto request = new AddTeamToTopicDto("CT1","ST1");

        long numberOfTopicsBefore = teamLinkRepository.findAllByLinkUUIDAndLinkType(topicUUID, "TOPIC").size();

        HttpEntity httpEntity = new HttpEntity(request, headers);
        ResponseEntity result = restTemplate.exchange(
                getBasePath() + "/topic/" + topicUUID + "/team/" + teamUUID
                , HttpMethod.POST, httpEntity, Void.class);

        long numberOfTopicsAfter = teamLinkRepository.findAllByLinkUUIDAndLinkType(topicUUID, "TOPIC").size();
        TeamLink teamLink = teamLinkRepository.findByLinkUUIDAndLinkTypeAndCaseTypeAndStageType(topicUUID, "TOPIC", "CT1", "ST1");


        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(teamLink).isNull();
        assertThat(numberOfTopicsAfter).isEqualTo(numberOfTopicsBefore);
    }

    @Test
    public void shouldErrorAndNotAddTeamWhenAddingInactiveTeamToTopic(){

        UUID topicUUID = UUID.fromString("11111111-ffff-1111-1111-111111111131");
        UUID inactiveTeamUUID = UUID.fromString("d09f1444-87ec-4197-8ec5-f28f548d11be");
        AddTeamToTopicDto request = new AddTeamToTopicDto("CT1","ST2");

        long numberOfTopicsBefore = teamLinkRepository.findAllByLinkUUIDAndLinkType(topicUUID, "TOPIC").size();

        HttpEntity httpEntity = new HttpEntity(request, headers);
        ResponseEntity result = restTemplate.exchange(
                getBasePath() + "/topic/" + topicUUID + "/team/" + inactiveTeamUUID
                , HttpMethod.POST, httpEntity, Void.class);

        long numberOfTopicsAfter = teamLinkRepository.findAllByLinkUUIDAndLinkType(topicUUID, "TOPIC").size();
        TeamLink teamLink = teamLinkRepository.findByLinkUUIDAndLinkTypeAndCaseTypeAndStageType(topicUUID, "TOPIC", "CT1", "ST2");

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        //assertThat(teamLink).isNull();
        assertThat(numberOfTopicsAfter).isEqualTo(numberOfTopicsBefore);
    }

    @Test
    public void shouldErrorAndNotAddTeamWhenAddingTeamToNonexistentTopic(){

        UUID topicUUID = UUID.fromString("11111111-ffff-1111-1111-11111110000");
        UUID teamUUID = UUID.fromString("8b3b4366-a37c-48b6-b274-4c50f8083843");
        AddTeamToTopicDto request = new AddTeamToTopicDto("CT1","ST1");

        long numberOfTopicsBefore = teamLinkRepository.findAllByLinkUUIDAndLinkType(topicUUID, "TOPIC").size();

        HttpEntity httpEntity = new HttpEntity(request, headers);
        ResponseEntity result = restTemplate.exchange(
                getBasePath() + "/topic/" + topicUUID + "/team/" + teamUUID
                , HttpMethod.POST, httpEntity, Void.class);

        long numberOfTopicsAfter = teamLinkRepository.findAllByLinkUUIDAndLinkType(topicUUID, "TOPIC").size();
        TeamLink teamLink = teamLinkRepository.findByLinkUUIDAndLinkTypeAndCaseTypeAndStageType(topicUUID, "TOPIC", "CT1", "ST1");

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(teamLink).isNull();
        assertThat(numberOfTopicsAfter).isEqualTo(numberOfTopicsBefore);
    }

    @Test
    public void shouldErrorAndNotAddTeamWhenAddingTeamToTopicWithInvalidCaseType(){

        UUID topicUUID = UUID.fromString("11111111-ffff-1111-1111-111111111133");
        UUID teamUUID = UUID.fromString("8b3b4366-a37c-48b6-b274-4c50f8083843");
        AddTeamToTopicDto request = new AddTeamToTopicDto("XYZ","DCU_MIN_INITIAL_DRAFT");

        long numberOfTopicsBefore = teamLinkRepository.findAllByLinkUUIDAndLinkType(topicUUID, "TOPIC").size();

        HttpEntity httpEntity = new HttpEntity(request, headers);
        ResponseEntity result = restTemplate.exchange(
                getBasePath() + "/topic/" + topicUUID + "/team/" + teamUUID
                , HttpMethod.POST, httpEntity, Void.class);

        long numberOfTopicsAfter = teamLinkRepository.findAllByLinkUUIDAndLinkType(topicUUID, "TOPIC").size();
        TeamLink teamLink = teamLinkRepository.findByLinkUUIDAndLinkTypeAndCaseTypeAndStageType(topicUUID, "TOPIC", "CT1", "ST1");

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(teamLink).isNull();
        assertThat(numberOfTopicsAfter).isEqualTo(numberOfTopicsBefore);
    }

    @Test
    public void shouldErrorAndNotAddTeamWhenAddingTeamToTopicWithInvalidStageType(){

        UUID topicUUID = UUID.fromString("11111111-ffff-1111-1111-111111111133");
        UUID teamUUID = UUID.fromString("8b3b4366-a37c-48b6-b274-4c50f8083843");
        AddTeamToTopicDto request = new AddTeamToTopicDto("CT1","INVALID_STAGE");

        long numberOfTopicsBefore = teamLinkRepository.findAllByLinkUUIDAndLinkType(topicUUID, "TOPIC").size();

        HttpEntity httpEntity = new HttpEntity(request, headers);
        ResponseEntity result = restTemplate.exchange(
                getBasePath() + "/topic/" + topicUUID + "/team/" + teamUUID
                , HttpMethod.POST, httpEntity, Void.class);

        long numberOfTopicsAfter = teamLinkRepository.findAllByLinkUUIDAndLinkType(topicUUID, "TOPIC").size();
        TeamLink teamLink = teamLinkRepository.findByLinkUUIDAndLinkTypeAndCaseTypeAndStageType(topicUUID, "TOPIC", "CT1", "ST1");

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(teamLink).isNull();
        assertThat(numberOfTopicsAfter).isEqualTo(numberOfTopicsBefore);
    }

    @Test
    public void shouldUpdateTeamWhenAddingTeamToTopicWhichAlreadyHasADifferentTeam(){

        UUID topicUUID = UUID.fromString("11111111-ffff-1111-1111-111111111131");
        UUID newTeamUUID = UUID.fromString("8b3b4366-a37c-48b6-b274-4c50f8083843");
        AddTeamToTopicDto request = new AddTeamToTopicDto("CT1","ST1");

        long numberOfTopicsBefore = teamLinkRepository.findAllByLinkUUIDAndLinkType(topicUUID, "TOPIC").size();

        HttpEntity httpEntity = new HttpEntity(request, headers);
        ResponseEntity result = restTemplate.exchange(
                getBasePath() + "/topic/" + topicUUID + "/team/" + newTeamUUID
                , HttpMethod.POST, httpEntity, Void.class);

        long numberOfTopicsAfter = teamLinkRepository.findAllByLinkUUIDAndLinkType(topicUUID, "TOPIC").size();

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(numberOfTopicsAfter).isEqualTo(numberOfTopicsBefore);
        assertThat(teamLinkRepository
                .findByLinkUUIDAndLinkTypeAndCaseTypeAndStageType(topicUUID, "TOPIC", "CT1", "ST1")
                .getResponsibleTeamUUID())
                .isEqualTo(newTeamUUID);
    }

    private String getBasePath() {
        return "http://localhost:" + port;
    }

}