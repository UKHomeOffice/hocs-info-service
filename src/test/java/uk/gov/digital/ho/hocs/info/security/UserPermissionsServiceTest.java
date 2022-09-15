package uk.gov.digital.ho.hocs.info.security;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import uk.gov.digital.ho.hocs.info.application.RequestData;

import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserPermissionsServiceTest {

    @Mock
    private RequestData requestData;

    private UserPermissionsService service;

    private String uuid1 = Base64UUID.uuidToBase64String(UUID.fromString("1325fe16-b864-42c7-85c2-7cab2863fe01"));

    private String uuid2 = Base64UUID.uuidToBase64String(UUID.fromString("f1825c7d-baff-4c09-8056-2166760ccbd2"));

    private String uuid3 = Base64UUID.uuidToBase64String(UUID.fromString("1c1e2f17-d5d9-4ff6-a023-6c40d76e1e9d"));

    @Before
    public void setup() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setMethod("GET");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    }

    @Test
    public void shouldIgnoreInvalidUserGroups() {

        String[] groups = (uuid2 + "," + uuid3 + "," + "INVALID_UUID").split(",");

        when(requestData.groupsArray()).thenReturn(groups);
        service = new UserPermissionsService(requestData);
        assertThat(service.getUserTeams()).size().isEqualTo(2);
    }

    @Test
    public void shouldGetTeamsForUser() {
        String[] groups = (uuid2 + "," + uuid3 + "," + uuid1).split(",");

        when(requestData.groupsArray()).thenReturn(groups);
        service = new UserPermissionsService(requestData);
        Set<UUID> teams = service.getUserTeams();
        assertThat(teams).size().isEqualTo(3);
        assertThat(teams).contains(UUID.fromString("1c1e2f17-d5d9-4ff6-a023-6c40d76e1e9d"));
        assertThat(teams).contains(UUID.fromString("f1825c7d-baff-4c09-8056-2166760ccbd2"));
        assertThat(teams).contains(UUID.fromString("1325fe16-b864-42c7-85c2-7cab2863fe01"));
    }

}