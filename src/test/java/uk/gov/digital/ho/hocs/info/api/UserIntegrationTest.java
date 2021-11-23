package uk.gov.digital.ho.hocs.info.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.SqlConfig.TransactionMode.ISOLATED;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.UUID;
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
import uk.gov.digital.ho.hocs.info.api.dto.CreateTopicDto;
import uk.gov.digital.ho.hocs.info.api.dto.CreateTopicResponse;
import uk.gov.digital.ho.hocs.info.api.dto.CreateUserDto;
import uk.gov.digital.ho.hocs.info.api.dto.CreateUserResponse;
import uk.gov.digital.ho.hocs.info.api.dto.UpdateUserDto;
import uk.gov.digital.ho.hocs.info.api.dto.UserDto;
import uk.gov.digital.ho.hocs.info.domain.repository.UnitRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = { "user.email.whitelist=homeoffice.gov.uk" },
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "classpath:beforeTest.sql", config = @SqlConfig(transactionMode = ISOLATED))
@Sql(scripts = "classpath:afterTest.sql", config = @SqlConfig(transactionMode = ISOLATED), executionPhase = AFTER_TEST_METHOD)
@ActiveProfiles("test")
public class UserIntegrationTest {

    TestRestTemplate restTemplate = new TestRestTemplate();

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
    public void shouldCreateUser() {

        //create
        CreateUserDto createUserDto = new CreateUserDto("aa" + System.currentTimeMillis() + "@homeoffice.gov.uk", "Bbbbb", "Ccccc");

        HttpEntity httpEntity = new HttpEntity(createUserDto, headers);
        ResponseEntity<CreateUserResponse> postResponse = restTemplate.exchange(
            getBasePath() + "/user", HttpMethod.POST, httpEntity, CreateUserResponse.class);
        CreateUserResponse createUserResponse = postResponse.getBody();

        //get
        ResponseEntity<UserDto> getResponse = restTemplate.exchange(
            getBasePath() + "/user/" + createUserResponse.getUserUUID(), HttpMethod.GET, httpEntity, UserDto.class);
        UserDto userDto = getResponse.getBody();
        assertThat(userDto.getEmail()).isEqualTo(createUserDto.getEmail());
        assertThat(userDto.getFirstName()).isEqualTo(createUserDto.getFirstName());
        assertThat(userDto.getLastName()).isEqualTo(createUserDto.getLastName());
        assertThat(userDto.isEnabled()).isEqualTo(true);
    }

    @Test
    public void shouldUpdateUser() {

        // create
        CreateUserDto createUserDto = new CreateUserDto("aa" + System.currentTimeMillis() + "@homeoffice.gov.uk", "Bbbbb", "Ccccc");
        HttpEntity httpEntity = new HttpEntity(createUserDto, headers);
        ResponseEntity<CreateUserResponse> postResponse = restTemplate.exchange(
            getBasePath() + "/user", HttpMethod.POST, httpEntity, CreateUserResponse.class);
        CreateUserResponse createUserResponse = postResponse.getBody();

        //update
        UpdateUserDto updateUserDto = new UpdateUserDto("Dddddd", "Eeeeee", false);
        HttpEntity httpEntity2 = new HttpEntity(updateUserDto, headers);
        restTemplate.exchange(
            getBasePath() + "/user/" + createUserResponse.getUserUUID(), HttpMethod.PUT, httpEntity2, String.class);

        //get
        ResponseEntity<UserDto> getResponse = restTemplate.exchange(
            getBasePath() + "/user/" + createUserResponse.getUserUUID(), HttpMethod.GET, httpEntity, UserDto.class);
        UserDto userDto = getResponse.getBody();
        assertThat(userDto.getEmail()).isEqualTo(createUserDto.getEmail());
        assertThat(userDto.getFirstName()).isEqualTo(updateUserDto.getFirstName());
        assertThat(userDto.getLastName()).isEqualTo(updateUserDto.getLastName());
        assertThat(userDto.isEnabled()).isEqualTo(updateUserDto.getEnabled());
    }

    private String getBasePath() {
        return "http://localhost:" + port;
    }
}


