package uk.gov.digital.ho.hocs.info.api;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uk.gov.digital.ho.hocs.info.api.TopicResource;
import uk.gov.digital.ho.hocs.info.api.TopicService;
import uk.gov.digital.ho.hocs.info.api.dto.*;
import uk.gov.digital.ho.hocs.info.domain.model.ParentTopic;
import uk.gov.digital.ho.hocs.info.domain.model.Topic;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TopicResourceTest {

    @Mock
    private TopicService topicService;

    private TopicResource topicResource;

    @Before
    public void setUp() {
        topicResource = new TopicResource(topicService);
    }

    @Test
    public void shouldGetAllTopics() {
        when(topicService.getTopics()).thenReturn(getTopics());

        ResponseEntity<Set<TopicDto>> response = topicResource.getTopics();

        verify(topicService, times(1)).getTopics();
        verifyNoMoreInteractions(topicService);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void shouldGetParentTopicsAndChildTopicsByCaseType() {
        when(topicService.getParentTopics(any())).thenReturn(getParentTopics());

        ResponseEntity<GetAllTopicsResponse> response = topicResource.getAllTopicsByCaseType("MIN");

        verify(topicService, times(1)).getParentTopics(any());
        verifyNoMoreInteractions(topicService);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void shouldGetParentTopicsByCaseType() {
        when(topicService.getParentTopics(any())).thenReturn(getParentTopics());

        ResponseEntity<GetParentTopicsResponse> response = topicResource.getAllParentTopicsByCaseType("MIN");

        verify(topicService, times(1)).getParentTopics(any());
        verifyNoMoreInteractions(topicService);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
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
        when(topicService.getTopic(any())).thenReturn(new Topic("Topic1", UUID.randomUUID()));

        ResponseEntity<TopicDto> response = topicResource.getTopicByUUID(UUID.randomUUID());

        verify(topicService, times(1)).getTopic(any());
        verifyNoMoreInteractions(topicService);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    private List<ParentTopic> getParentTopics() {
        return new ArrayList<ParentTopic>() {{
            add(new ParentTopic(1l, UUID.randomUUID(), "ParentTopic1", new HashSet<>(), true));
            add(new ParentTopic(2l, UUID.randomUUID(), "ParentTopic2", new HashSet<>(), true));
        }};
    }

    private List<Topic> getTopics() {
        return new ArrayList<Topic>() {{
            add(new Topic("Topic1", UUID.randomUUID()));
            add(new Topic("Topic2", UUID.randomUUID()));
        }};
    }

    @Test
    public void shouldCreateParentTopic() {

        CreateParentTopicDto request = new CreateParentTopicDto("ParentTopic");

        when(topicService.createParentTopic(request)).thenReturn(UUID.randomUUID());

        ResponseEntity<CreateParentTopicResponse> response = topicResource.createParentTopic(request);

        verify(topicService, times(1)).createParentTopic(any());
        verifyNoMoreInteractions(topicService);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void shouldCreateTopic() {

        CreateTopicDto request = new CreateTopicDto("ParentTopic");

        when(topicService.createTopic(any(), any())).thenReturn(UUID.randomUUID());

        ResponseEntity<CreateTopicResponse> response = topicResource.createTopic(request, UUID.randomUUID());

        verify(topicService, times(1)).createTopic(any(), any());
        verifyNoMoreInteractions(topicService);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void shouldDeleteParentTopic() {

        ResponseEntity response = topicResource.deleteParentTopic(UUID.randomUUID());

        verify(topicService, times(1)).deleteParentTopic(any());
        verifyNoMoreInteractions(topicService);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void shouldDeleteTopic() {

        ResponseEntity response = topicResource.deleteTopic(UUID.randomUUID());

        verify(topicService, times(1)).deleteTopic(any());
        verifyNoMoreInteractions(topicService);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void shouldReactivateParentTopic() {

        ResponseEntity response = topicResource.reactivateParentTopic(UUID.randomUUID());

        verify(topicService, times(1)).reactivateParentTopic(any());
        verifyNoMoreInteractions(topicService);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void shouldReactivateTopic() {

        ResponseEntity response = topicResource.reactivateTopic(UUID.randomUUID());

        verify(topicService, times(1)).reactivateTopic(any());
        verifyNoMoreInteractions(topicService);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

}