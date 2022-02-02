package uk.gov.digital.ho.hocs.info.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import uk.gov.digital.ho.hocs.info.api.dto.CreateUserDto;

import java.util.List;

// NB. This sort of test reads the spring properties files.
@RunWith(SpringRunner.class)
@WebMvcTest(UserResource.class)
public class UserResourceRestTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private UserService userService;

  private final ObjectMapper mapper = new ObjectMapper();

  @Test
  public void testCreateUserWithWhitelistedDomains() throws Exception {
    List<String> whitelistedDomains = List.of("mick@homeoffice.gov.uk");

    for (String domain :
            whitelistedDomains) {
      CreateUserDto user = new CreateUserDto(domain, "firstName", "lastName");
      mockMvc.perform(post("/user")
              .content(mapper.writeValueAsString(user))
              .contentType(APPLICATION_JSON_UTF8)
      ).andExpect(status().isOk());
    }
  }

  @Test
  public void testCreateUserWithNonWhitelistedDomain() throws Exception {
    CreateUserDto user = new CreateUserDto("mick@test.com", "firstName", "lastName");
    String res = mockMvc.perform(post("/user")
        .content(mapper.writeValueAsString(user))
        .contentType(APPLICATION_JSON_UTF8)
    )
        .andExpect(status().isBadRequest())
        .andReturn()
        .getResponse()
        .getContentAsString();
    assertThat(res).isEqualTo("Email domain not supported");
  }
}
