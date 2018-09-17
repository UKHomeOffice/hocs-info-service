package uk.gov.digital.ho.hocs.info.topic;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uk.gov.digital.ho.hocs.info.dto.GetParentTopicsResponse;
import uk.gov.digital.ho.hocs.info.dto.GetTopicsResponse;
import uk.gov.digital.ho.hocs.info.dto.TopicDto;
import uk.gov.digital.ho.hocs.info.entities.ParentTopic;
import uk.gov.digital.ho.hocs.info.entities.Topic;
import uk.gov.digital.ho.hocs.info.exception.EntityNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TopicResourceTest {

    @Mock
    private TopicService topicService;

    private TopicResource topicResource;

    private static final String[] ROLES = {"Create", "DCU"};

    @Before
    public void setUp() {
        topicResource = new TopicResource(topicService);
    }

    @Test
    public void shouldGetParentTopicsByCaseType() {

        when(topicService.getParentTopics(any())).thenReturn(getParentTopics());

        ResponseEntity<GetParentTopicsResponse> response = topicResource.getAllParentTopicsByCaseType("MIN");

        verify(topicService, times(1)).getParentTopics(any());
        verifyNoMoreInteractions(topicService);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getParentTopicDto().size()).isEqualTo(2);
        assertThat(response.getBody().getParentTopicDto().get(0).getDisplayName()).isEqualTo("ParentTopic1");
        assertThat(response.getBody().getParentTopicDto().get(1).getDisplayName()).isEqualTo("ParentTopic2");
    }

    @Test
    public void shouldGetTopicsByParentTopics() {
        when(topicService.getAllTopicsForParentTopic(any())).thenReturn(getTopics());

        ResponseEntity<GetTopicsResponse> response = topicResource.getTopicsByParentTopic(UUID.randomUUID());

        verify(topicService, times(1)).getAllTopicsForParentTopic(any());
        verifyNoMoreInteractions(topicService);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void getTopicByUUID() {

        when(topicService.getTopic(any())).thenReturn(new Topic(1, "Topic1", UUID.randomUUID()));

        ResponseEntity<TopicDto> response = topicResource.getTopicByUUID(UUID.randomUUID());

        verify(topicService, times(1)).getTopic(any());
        verifyNoMoreInteractions(topicService);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }



    private List<ParentTopic> getParentTopics() {
        return new ArrayList<ParentTopic>() {{
            add(new ParentTopic(1, "ParentTopic1", UUID.randomUUID()));
            add(new ParentTopic(2, "ParentTopic2", UUID.randomUUID()));
        }};

    }

    private List<Topic> getTopics() {
        return new ArrayList<Topic>() {{
            add(new Topic(1, "Topic1", UUID.randomUUID()));
            add(new Topic(2, "Topic2", UUID.randomUUID()));
        }};

    }

}