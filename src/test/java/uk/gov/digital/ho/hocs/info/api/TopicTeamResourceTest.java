package uk.gov.digital.ho.hocs.info.api;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uk.gov.digital.ho.hocs.info.api.dto.*;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TopicTeamResourceTest {

    @Mock
    private TopicTeamService topicTeamService;

    private TopicTeamResource topicTeamResource;

    @Before
    public void setUp() {
        topicTeamResource = new TopicTeamResource(topicTeamService);
    }

    @Test
    public void shouldAddTeamToTopic() {

        AddTeamToTopicDto request = new AddTeamToTopicDto("MIN", "DCU_MIN_MARKUP");
        UUID teamUUID = UUID.randomUUID();
        UUID topicUUID = UUID.randomUUID();

        ResponseEntity response = topicTeamResource.addTeamToTopic(topicUUID, teamUUID, request);
        verify(topicTeamService, times(1)).addTeamToTopic(topicUUID, teamUUID, request);
        verifyNoMoreInteractions(topicTeamService);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void shouldUpdateTeamForTopic() {

        AddTeamToTopicDto request = new AddTeamToTopicDto("MIN", "DCU_MIN_MARKUP");
        UUID teamUUID = UUID.randomUUID();
        UUID topicUUID = UUID.randomUUID();

        ResponseEntity response = topicTeamResource.updateTeamForTopic(topicUUID, teamUUID, request);
        verify(topicTeamService, times(1)).updateTeamForTopic(topicUUID, teamUUID, request);
        verifyNoMoreInteractions(topicTeamService);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

}