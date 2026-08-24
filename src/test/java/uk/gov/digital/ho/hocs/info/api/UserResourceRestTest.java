package uk.gov.digital.ho.hocs.info.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.MockMvc;
import uk.gov.digital.ho.hocs.info.api.dto.CreateUserDto;
import uk.gov.digital.ho.hocs.info.api.dto.UserDto;

import java.util.List;
import java.util.stream.Stream;

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

        for (String domain : whitelistedDomains) {
            CreateUserDto user = new CreateUserDto(domain, "firstName", "lastName");
            mockMvc.perform(
                post("/user").content(mapper.writeValueAsString(user)).contentType(APPLICATION_JSON_UTF8)).andExpect(
                status().isOk());
        }
    }

    @Test
    public void testCreateUserWithNonWhitelistedDomain() throws Exception {
        CreateUserDto user = new CreateUserDto("mick@test.com", "firstName", "lastName");
        String res = mockMvc.perform(
            post("/user").content(mapper.writeValueAsString(user)).contentType(APPLICATION_JSON_UTF8)).andExpect(
            status().isBadRequest()).andReturn().getResponse().getContentAsString();
        assertThat(res).isEqualTo("Email domain not supported");
    }

    @Test
    public void testGetAllUsersReturnsJsonStream() throws Exception {
        when(userService.streamAllUsers()).thenReturn(Stream.of(
            new UserDto("1", "user1", "user1@test.com", "First", "User", true)
        ));

        MvcResult result = mockMvc.perform(get("/users"))
            .andExpect(request().asyncStarted())
            .andReturn();

        mockMvc.perform(asyncDispatch(result))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json"))
            .andExpect(content().json("[{\"id\":\"1\",\"username\":\"user1\",\"email\":\"user1@test.com\",\"firstName\":\"First\",\"lastName\":\"User\",\"enabled\":true}]"));
    }

}
