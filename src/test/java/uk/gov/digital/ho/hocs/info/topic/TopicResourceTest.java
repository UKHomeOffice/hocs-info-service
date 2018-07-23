package uk.gov.digital.ho.hocs.info.topic;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uk.gov.digital.ho.hocs.info.dto.GetTopicsResponse;
import uk.gov.digital.ho.hocs.info.entities.Minister;
import uk.gov.digital.ho.hocs.info.entities.ParentTopic;
import uk.gov.digital.ho.hocs.info.entities.Topic;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@SuppressFBWarnings("NP_NULL_ON_SOME_PATH_FROM_RETURN_VALUE")
public class TopicResourceTest {

    @Mock
    private TopicService topicService;

    private TopicResource topicResource;

    private static final String[] ROLES= {"Create","DCU"};

    @Before
    public void setUp() {
        topicResource = new TopicResource(topicService);
    }

    @Test
    public void shouldGetTopicsByTenant() {

        when(topicService.getTopics(any())).thenReturn( getTopics());

        ResponseEntity<GetTopicsResponse> response = topicResource.getAllTopics(ROLES);

        verify(topicService, times(1)).getTopics(anyList());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getTopics().size()).isEqualTo(1);
        assertThat(response.getBody().getTopics().get(0).getDisplayName()).isEqualTo("Parent Topic 1");

        List<Topic> responseTopicAsList = new ArrayList<>(response.getBody().getTopics().get(0).getTopic());

        Topic result1 = responseTopicAsList.stream().filter(x -> Objects.equals(1 ,x.getId())).findAny().orElse(null);
        assertThat(result1).isNotNull();
        assertThat(result1.getDisplayName()).isEqualTo("Topic1");
        Topic result2 = responseTopicAsList.stream().filter(x -> Objects.equals(2,x.getId())).findAny().orElse(null);
        assertThat(result2).isNotNull();
        assertThat(result2.getDisplayName()).isEqualTo("Topic2");
    }

    private List<ParentTopic> getTopics() {
        Set<Topic> topics = new HashSet<Topic>() {{
                add(new Topic(1, "Topic1", new Minister(1, "Minister1")));
                add(new Topic(2, "Topic2", new Minister(2, "Minister2")));
            }};

        return new ArrayList<ParentTopic>() {{
            add(new ParentTopic(1,"Parent Topic 1", topics));
        }};
    }
}