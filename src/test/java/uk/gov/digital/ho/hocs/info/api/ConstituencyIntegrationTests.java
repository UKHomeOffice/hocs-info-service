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
import uk.gov.digital.ho.hocs.info.api.dto.CreateConstituencyDto;
import uk.gov.digital.ho.hocs.info.api.dto.CreateConstituencyResponse;
import uk.gov.digital.ho.hocs.info.domain.model.Constituency;
import uk.gov.digital.ho.hocs.info.domain.repository.ConstituencyRepository;

import java.util.Collection;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.SqlConfig.TransactionMode.ISOLATED;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "classpath:beforeTest.sql", config = @SqlConfig(transactionMode = ISOLATED))
@Sql(scripts = "classpath:afterTest.sql", config = @SqlConfig(transactionMode = ISOLATED), executionPhase = AFTER_TEST_METHOD)
@ActiveProfiles("test")
public class ConstituencyIntegrationTests {

    TestRestTemplate restTemplate = new TestRestTemplate();

    @Autowired
    ConstituencyRepository constituencyRepository;

    private HttpHeaders headers;

    @LocalServerPort
    int port;

    final UUID constituencyUUID = UUID.fromString("11111111-eeee-1111-1111-111111111111");

    @Before
    public void setup(){
        headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.toString());
    }

    @Test
    public void shouldReturnRegionWhenSet() {
        Constituency constituencyWithRegion = constituencyRepository.findConstituencyByName("test constituency 1");
        assertThat(constituencyWithRegion).isNotNull();
        assertThat(constituencyWithRegion.getRegion()).isNotNull();
        assertThat(constituencyWithRegion.getRegion().getRegionName()).isEqualTo("region");

        Constituency constituencyWithoutRegion = constituencyRepository.findConstituencyByName("test constituency 2");
        assertThat(constituencyWithoutRegion).isNotNull();
        assertThat(constituencyWithoutRegion.getRegion()).isNull();
    }

    @Test
    public void shouldCreateConstituency() {

        long numberOfConstituencysBefore = constituencyRepository.count();

        CreateConstituencyDto request = new CreateConstituencyDto("test constituency");

        HttpEntity httpEntity = new HttpEntity(request, headers);
        ResponseEntity<CreateConstituencyResponse> result = restTemplate.exchange(
                getBasePath() + "/constituency"
                , HttpMethod.POST, httpEntity, CreateConstituencyResponse.class);

        long numberOfConstituencysAfter = ((Collection<Constituency>)constituencyRepository.findAll()).size();

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isNotNull();
        assertThat(numberOfConstituencysAfter).isEqualTo(numberOfConstituencysBefore + 1l);
    }

    @Test
    public void shouldNotCreateConstituencyWhenActiveConstituencyWithSameNameAsGiven() {

        long numberOfConstituencysBefore = constituencyRepository.count();

        CreateConstituencyDto request = new CreateConstituencyDto("test constituency 1");

        HttpEntity httpEntity = new HttpEntity(request, headers);
        ResponseEntity<Void> result = restTemplate.exchange(
                getBasePath() + "/constituency"
                , HttpMethod.POST, httpEntity, Void.class);

        long numberOfConstituencysAfter = constituencyRepository.count();

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(numberOfConstituencysAfter).isEqualTo(numberOfConstituencysBefore);
    }

    @Test
    public void shouldMakeConstituencyInactive(){

        UUID constituencyUUID1 = UUID.fromString("11111111-eeee-1111-1111-111111111111");
        UUID constituencyUUID2 = UUID.fromString("11111111-eeee-1111-1111-111111111112");

        HttpEntity httpEntity = new HttpEntity(headers);

        ResponseEntity<Void> result = restTemplate.exchange(
                getBasePath() + "/constituency/" + constituencyUUID1
                , HttpMethod.DELETE, httpEntity, Void.class);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(constituencyRepository.findConstituencyByUUID(constituencyUUID1).isActive()).isFalse();
        assertThat(constituencyRepository.findConstituencyByUUID(constituencyUUID2).isActive()).isTrue();
    }

    @Test
    public void shouldReactivateInactiveConstituency() {

        UUID inactiveConstituencyUUID = UUID.fromString("11111111-eeee-1111-1111-111111111114");

        HttpEntity httpEntity = new HttpEntity(headers);
        ResponseEntity result = restTemplate.exchange(
                getBasePath() + "/constituency/" + inactiveConstituencyUUID
                , HttpMethod.PUT, httpEntity, Void.class);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(constituencyRepository.findConstituencyByUUID(inactiveConstituencyUUID).isActive()).isEqualTo(true);
    }

    @Test
    public void shouldNotChangeActiveConstituencyWhenAlreadyActive() {

        Constituency constituencyBefore = constituencyRepository.findConstituencyByUUID(constituencyUUID);

        HttpEntity httpEntity = new HttpEntity(headers);
        ResponseEntity result = restTemplate.exchange(
                getBasePath() + "/constituency/" + constituencyUUID
                , HttpMethod.PUT, httpEntity, Void.class);

        Constituency constituencyAfter = constituencyRepository.findConstituencyByUUID(constituencyUUID);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(constituencyBefore.isActive()).isEqualTo(constituencyAfter.isActive());

    }

    private String getBasePath() {
        return "http://localhost:" + port;
    }
}
