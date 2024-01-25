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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import uk.gov.digital.ho.hocs.info.api.dto.CreateNominatedContactDto;
import uk.gov.digital.ho.hocs.info.api.dto.CreateNominatedContactResponse;
import uk.gov.digital.ho.hocs.info.api.dto.UpdateNominatedContactDto;
import uk.gov.digital.ho.hocs.info.domain.model.NominatedContact;
import uk.gov.digital.ho.hocs.info.domain.repository.NominatedContactRepository;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

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
public class NominatedContactIntegrationTests {

    TestRestTemplate restTemplate = new TestRestTemplate();

    @Autowired
    NominatedContactRepository nominatedContactRepository;

    private final UUID teamUUID = UUID.fromString("08612f06-bae2-4d2f-90d2-2254a68414b8");

    private HttpHeaders headers;

    @LocalServerPort
    int port;

    @Autowired
    ObjectMapper mapper;

    @Before
    public void setup() throws IOException {
        headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.toString());
    }

    @Test
    public void shouldGetAllNominatedContactsForTeam() {

        HttpEntity httpEntity = new HttpEntity(headers);
        ResponseEntity<Set<NominatedContact>> result = restTemplate.exchange(
            getBasePath() + "/team/" + teamUUID.toString() + "/contact", HttpMethod.GET, httpEntity,
            new ParameterizedTypeReference<Set<NominatedContact>>() {});

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody().size()).isEqualTo(2);

    }

    @Test
    public void shouldCreateNominatedContactForTeam() {

        String email = "new@email.com";
        CreateNominatedContactDto request = new CreateNominatedContactDto(email);
        HttpEntity<CreateNominatedContactDto> httpEntity = new HttpEntity(request, headers);

        ResponseEntity<CreateNominatedContactResponse> result = restTemplate.exchange(
            getBasePath() + "/team/" + teamUUID.toString() + "/contact", HttpMethod.POST, httpEntity,
            CreateNominatedContactResponse.class);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        UUID uuid = UUID.fromString(result.getBody().getUuid());
        assertThat(nominatedContactRepository.findByUuid(uuid)).isNotNull();

    }

    @Test
    public void shouldUpdateNominatedContactForTeam() {

        String email = "new@email.com";
        String contactUUID = "10c88bbc-00a7-4226-9d7b-bae5186debcd";
        UpdateNominatedContactDto request = new UpdateNominatedContactDto(email);
        HttpEntity<UpdateNominatedContactDto> httpEntity = new HttpEntity(request);

        ResponseEntity result = restTemplate.exchange(
            getBasePath() + "/team/" + "434a4e33-437f-4e6d-8f04-14ea40fdbfa2" + "/contact/" + contactUUID,
            HttpMethod.PUT, httpEntity, String.class);

        NominatedContact contact = nominatedContactRepository.findByUuid(UUID.fromString(contactUUID));
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(contact.getEmailAddress()).isEqualTo(email);

    }

    @Test
    public void shouldSuccessfullyDeleteNominatedContactForTeam() {

        String contactUUID = "37f76a1d-5366-4ab7-a7b1-a23b68c95b89";

        HttpEntity httpEntity = new HttpEntity(headers);

        ResponseEntity result = restTemplate.exchange(
            getBasePath() + "/team/" + teamUUID.toString() + "/contact/" + contactUUID, HttpMethod.DELETE, httpEntity,
            String.class);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(nominatedContactRepository.findByUuid(UUID.fromString(contactUUID))).isNull();

    }

    @Test
    public void shouldReturnErrorWhenTryingToDeleteTheOnlyNominatedContactFromATeam() {

        String teamUUID = "911adabe-5ab7-4470-8395-6b584a61462d";
        String contactUUID = "8bc0e84d-08e0-42f2-9d75-ff7b7c40d9fa";

        HttpEntity httpEntity = new HttpEntity(headers);

        ResponseEntity result = restTemplate.exchange(getBasePath() + "/team/" + teamUUID + "/contact/" + contactUUID,
            HttpMethod.DELETE, httpEntity, String.class);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(nominatedContactRepository.findByUuid(UUID.fromString(contactUUID))).isNotNull();

    }

    private String getBasePath() {
        return "http://localhost:" + port;
    }

}
