package uk.gov.digital.ho.hocs.info.api;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.digital.ho.hocs.info.api.TopicService;
import uk.gov.digital.ho.hocs.info.api.dto.CreateParentTopicDto;
import uk.gov.digital.ho.hocs.info.api.dto.CreateTopicDto;
import uk.gov.digital.ho.hocs.info.client.caseworkclient.CaseworkClient;
import uk.gov.digital.ho.hocs.info.domain.exception.ApplicationExceptions;
import uk.gov.digital.ho.hocs.info.domain.model.ParentTopic;
import uk.gov.digital.ho.hocs.info.domain.model.Topic;
import uk.gov.digital.ho.hocs.info.domain.repository.ParentTopicRepository;
import uk.gov.digital.ho.hocs.info.domain.repository.TopicRepository;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TopicServiceTest {

    @Mock
    private ParentTopicRepository parentTopicRepository;

    @Mock
    private TopicRepository topicRepository;

    @Mock
    private CaseworkClient caseworkClient;

    private TopicService topicService;

    private UUID uuid = UUID.randomUUID();
    private UUID parentUuid = UUID.randomUUID();

    @Before
    public void setUp() {
        this.topicService = new TopicService(parentTopicRepository, topicRepository, caseworkClient);
    }

    @Test
    public void shouldReturnParentTopicsForCaseType() {
        when(parentTopicRepository.findAllParentTopicByCaseType(any())).thenReturn(getParentTopics());

        topicService.getParentTopics("MIN");

        verify(parentTopicRepository, times(1)).findAllParentTopicByCaseType(any());
        verifyNoMoreInteractions(parentTopicRepository);
    }

    @Test
    public void shouldReturnTopicsForParentTopic() {
        when(topicRepository.findTopicByParentTopic(uuid)).thenReturn(getTopics());

        topicService.getAllTopicsForParentTopic(uuid);

        verify(topicRepository, times(1)).findTopicByParentTopic(uuid);
        verifyNoMoreInteractions(parentTopicRepository);
    }

    @Test
    public void shouldReturnTopicsByCaseType() {
        when(topicRepository.findTopicByUUID(uuid)).thenReturn(new Topic("Topic1", parentUuid));

        topicService.getTopic(uuid);

        verify(topicRepository, times(1)).findTopicByUUID(uuid);
        verifyNoMoreInteractions(parentTopicRepository);
    }

    @Test
    public void shouldCreateParentTopic() {

        CreateParentTopicDto request = new CreateParentTopicDto("ParentTopic");

        when(parentTopicRepository.findByDisplayName(any())).thenReturn(null);

        topicService.createParentTopic(request);

        verify(parentTopicRepository, times(1)).findByDisplayName(any());
        verify(parentTopicRepository, times(1)).save(any());
        verifyNoMoreInteractions(parentTopicRepository);
    }


    @Test (expected = ApplicationExceptions.TopicCreationException.class)
    public void shouldNotCreateParentTopicWhenOneExistsWithGivenName() {

        ParentTopic parentTopic = new ParentTopic("ParentTopic");
        CreateParentTopicDto request = new CreateParentTopicDto("ParentTopic");

        when(parentTopicRepository.findByDisplayName(any())).thenReturn(parentTopic);

        topicService.createParentTopic(request);

        verify(parentTopicRepository, times(1)).findByDisplayName(any());
        verifyNoMoreInteractions(parentTopicRepository);
    }

    @Test
    public void shouldCreateTopicWithValidParent() {

        CreateTopicDto request = new CreateTopicDto("Topic");

        when(parentTopicRepository.findByUuid(any())).thenReturn(new ParentTopic("ParentTopic"));

        topicService.createTopic(request, UUID.randomUUID() );

        verify(topicRepository, times(1)).findTopicByNameAndParentTopic(any(), any());
        verify(topicRepository, times(1)).save(any());
        verifyNoMoreInteractions(topicRepository);
    }

    @Test (expected = ApplicationExceptions.TopicCreationException.class)
    public void shouldNotCreateTopicWithNonexistentParent() {

        CreateTopicDto request = new CreateTopicDto("Topic");

        when(parentTopicRepository.findByUuid(any())).thenReturn(null);

        topicService.createTopic(request, UUID.randomUUID());

        verifyZeroInteractions(topicRepository);
    }

    @Test (expected = ApplicationExceptions.TopicCreationException.class)
    public void shouldNotCreateTopicWithInactiveParent() {

        CreateTopicDto request = new CreateTopicDto("Topic");
        ParentTopic parentTopic = new ParentTopic("ParentTopic", false);

        when(parentTopicRepository.findByUuid(any())).thenReturn(parentTopic);

        topicService.createTopic(request, UUID.randomUUID());

        verifyZeroInteractions(topicRepository);
    }


    @Test (expected = ApplicationExceptions.TopicCreationException.class)
    public void shouldNotCreateTopicWhenParentAlreadyHasTopicWithGivenName() {

        CreateTopicDto request = new CreateTopicDto("Topic");
        ParentTopic parentTopic = new ParentTopic("ParentTopic", false);
        Topic topic = new Topic("Topic", UUID.randomUUID());

        when(parentTopicRepository.findByUuid(any())).thenReturn(parentTopic);
        when(topicRepository.findTopicByNameAndParentTopic(any(), any())).thenReturn(topic);

        topicService.createTopic(request, UUID.randomUUID());

        verify(topicRepository, times(1)).findTopicByNameAndParentTopic(any(), any());
        verifyNoMoreInteractions(topicRepository);
    }


    @Test
    public void shouldDeleteTopic() {
        when(topicRepository.findTopicByUUID(any())).thenReturn(new Topic("Topic", UUID.randomUUID()));

        topicService.deleteTopic(UUID.randomUUID());

        verify(topicRepository, times(1)).save(any());
        verify(topicRepository, times(1)).findTopicByUUID(any());
        verifyNoMoreInteractions(topicRepository);
    }


    @Test (expected = ApplicationExceptions.EntityNotFoundException.class)
    public void shouldNotDeleteTopicWithInvalidUUID() {
        when(topicRepository.findTopicByUUID(any())).thenReturn(null);

        topicService.deleteTopic(UUID.randomUUID());

        verify(topicRepository, times(1)).findTopicByUUID(any());
        verifyNoMoreInteractions(topicRepository);
    }

    @Test
    public void shouldDeleteParentTopicWithNoChildrenTopics() {
        when(parentTopicRepository.findByUuid(any())).thenReturn(new ParentTopic("Parent topic"));
        when(topicRepository.findAllActiveTopicsByParentTopic(UUID.randomUUID())).thenReturn(new HashSet<>());

        topicService.deleteParentTopic(UUID.randomUUID());

        verify(parentTopicRepository, times(1)).findByUuid(any());
        verify(parentTopicRepository, times(1)).save(any());
        verifyNoMoreInteractions(parentTopicRepository);

        verify(topicRepository, times(1)).findAllActiveTopicsByParentTopic(any());
        verifyNoMoreInteractions(topicRepository);
    }

    @Test
    public void shouldDeleteParentTopicWithChildrenTopics() {

        Set<Topic> topics = new HashSet<>();
        topics.add(new Topic("Topic", UUID.randomUUID()));

        when(parentTopicRepository.findByUuid(any())).thenReturn(new ParentTopic("Parent topic"));
        when(topicRepository.findAllActiveTopicsByParentTopic(any())).thenReturn(topics);

        topicService.deleteParentTopic(UUID.randomUUID());

        verify(parentTopicRepository, times(1)).findByUuid(any());
        verify(parentTopicRepository, times(1)).save(any());
        verifyNoMoreInteractions(parentTopicRepository);

        verify(topicRepository, times(1)).findAllActiveTopicsByParentTopic(any());
        verify(topicRepository, times(1)).save(any());

        verifyNoMoreInteractions(topicRepository);
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
}