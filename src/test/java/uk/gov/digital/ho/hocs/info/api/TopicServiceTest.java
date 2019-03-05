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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

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

//    @Test
//    public void shouldCreateTopicWithValidParent() {
//
//        CreateTopicDto request = new CreateTopicDto("ParentTopic", UUID.randomUUID());
//
//        when(parentTopicRepository.findByUUID(any())).thenReturn(new ParentTopic("ParentTopic"));
//        doNothing().when(topicRepository.save(any()));
//
//        topicService.createTopic(request);
//
//        verify(topicRepository, times(1)).save(any());
//        verifyNoMoreInteractions(topicRepository);
//    }
//
//    @Test (expected = ApplicationExceptions.TopicAlreadyExistsException.class)
//    public void shouldNotCreateTopicWithNonexistentParent() {
//
//        CreateTopicDto request = new CreateTopicDto("ParentTopic", UUID.randomUUID());
//
//        when(parentTopicRepository.findByUUID(any())).thenReturn(null);
//
//        topicService.createTopic(request);
//
//        verifyZeroInteractions(topicRepository);
//    }
//
//    @Test (expected = ApplicationExceptions.TopicAlreadyExistsException.class)
//    public void shouldCreateTopicWithInactiveParent() {
//
//        CreateTopicDto request = new CreateTopicDto("ParentTopic", UUID.randomUUID());
//
//        ParentTopic parentTopic = new ParentTopic("ParentTopic", false);
//        when(parentTopicRepository.findByUUID(any())).thenReturn(parentTopic);
//
//        topicService.createTopic(request);
//
//        verifyZeroInteractions(topicRepository);
//    }

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