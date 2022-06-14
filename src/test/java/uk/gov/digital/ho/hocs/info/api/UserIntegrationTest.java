package uk.gov.digital.ho.hocs.info.api;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import org.apache.http.entity.ContentType;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import uk.gov.digital.ho.hocs.info.api.dto.CreateUserDto;
import uk.gov.digital.ho.hocs.info.api.dto.CreateUserResponse;
import uk.gov.digital.ho.hocs.info.api.dto.UpdateUserDto;
import uk.gov.digital.ho.hocs.info.api.dto.UserDto;
import uk.gov.digital.ho.hocs.info.utils.BaseKeycloakTest;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserIntegrationTest extends BaseKeycloakTest {

    TestRestTemplate restTemplate = new TestRestTemplate();

    private HttpHeaders headers;

    @LocalServerPort
    int port;

    @Autowired
    ObjectMapper mapper;

    @BeforeEach
    public void setup() throws IOException {
        headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.toString());
    }

    @Test
    public void shouldCreateUser() {
        //create
        var createUserDto = new CreateUserDto("1@example.com", "A", "B");

        HttpEntity<CreateUserDto> httpEntity = new HttpEntity<>(createUserDto, headers);
        ResponseEntity<CreateUserResponse> postResponse = restTemplate.exchange(
            getBasePath() + "/user", HttpMethod.POST, httpEntity, CreateUserResponse.class);
        CreateUserResponse createUserResponse = postResponse.getBody();

        assertThat(createUserResponse).isNotNull();
        assertThat(createUserResponse.getUserUUID()).isNotNull();

        //get
        ResponseEntity<UserDto> getResponse = restTemplate.exchange(
            getBasePath() + "/user/" + createUserResponse.getUserUUID(), HttpMethod.GET, httpEntity, UserDto.class);
        UserDto userDto = getResponse.getBody();
        assertThat(userDto.getEmail()).isEqualTo(createUserDto.getEmail());
        assertThat(userDto.getFirstName()).isEqualTo(createUserDto.getFirstName());
        assertThat(userDto.getLastName()).isEqualTo(createUserDto.getLastName());
        assertThat(userDto.isEnabled()).isTrue();
    }

    @Test
    public void shouldReturn409_WhenUserExists() {
        //create
        var createUserDto = new CreateUserDto("1@example.com", "A", "B");

        // the existing tests are using a real Keycloak instance rather than a mock
        // so create the user first and then trying adding a duplicate to trigger the error
        HttpEntity<CreateUserDto> httpEntity = new HttpEntity<>(createUserDto, headers);
        restTemplate.exchange(
                getBasePath() + "/user", HttpMethod.POST, httpEntity, CreateUserResponse.class);
        ResponseEntity<Void> postResponse = restTemplate.exchange(
                getBasePath() + "/user", HttpMethod.POST, httpEntity, Void.class);

        assertThat(postResponse.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    public void shouldUpdateUser() {
        // create
        var createUserDto = new CreateUserDto("1@example.com", "A", "B");

        HttpEntity<CreateUserDto> httpEntity = new HttpEntity<>(createUserDto, headers);
        ResponseEntity<CreateUserResponse> postResponse = restTemplate.exchange(
            getBasePath() + "/user", HttpMethod.POST, httpEntity, CreateUserResponse.class);
        CreateUserResponse createUserResponse = postResponse.getBody();

        //update
        UpdateUserDto updateUserDto = new UpdateUserDto("A1", "B1", false);
        HttpEntity<UpdateUserDto> httpEntity2 = new HttpEntity<>(updateUserDto, headers);
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
