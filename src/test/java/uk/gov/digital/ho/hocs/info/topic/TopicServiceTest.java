package uk.gov.digital.ho.hocs.info.topic;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.digital.ho.hocs.info.entities.ParentTopic;
import uk.gov.digital.ho.hocs.info.entities.Tenant;
import uk.gov.digital.ho.hocs.info.entities.Topic;
import uk.gov.digital.ho.hocs.info.exception.EntityNotFoundException;
import uk.gov.digital.ho.hocs.info.repositories.ParentTopicRepository;
import uk.gov.digital.ho.hocs.info.repositories.TenantRepository;
import uk.gov.digital.ho.hocs.info.tenant.TenantService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TopicServiceTest {
    
    @Mock
    private ParentTopicRepository parentTopicRepository;

    @Mock
    private TenantService tenantService;

    private TopicService topicService;

    @Before
    public void setUp() { 
        this.topicService = new TopicService(parentTopicRepository,tenantService );
    }

    @Test(expected = EntityNotFoundException.class)
    public void shouldThrowExceptionWhenGetCaseTypesWithNoRoles() throws EntityNotFoundException {
        topicService.getTopics(null);
    }
    
    @Test
    public void shouldGetTopicsByTenant(){

        when(tenantService.getTenantsFromRoles(any())).thenReturn(new ArrayList<String>() {{ add("DCU");}});
        when(parentTopicRepository.findParentTopicByTenant("DCU")).thenReturn(getTopics());

        List<ParentTopic> parentTopics = topicService.getTopics(new ArrayList<String>() {{
            add("DCU");
        }});

        verify(parentTopicRepository, times(1)).findParentTopicByTenant(any());

        assertThat(parentTopics.size()).isEqualTo(1);
        assertThat(parentTopics.get(0).getId()).isEqualTo(1);
        assertThat(parentTopics.get(0).getDisplayName()).isEqualTo("Parent Topic 1");
        assertThat(parentTopics.get(0).getTopic().size()).isEqualTo(2);

        List<Topic> responseTopicAsList = new ArrayList<>(parentTopics.get(0).getTopic());

        Topic result1 = responseTopicAsList.stream().filter(x -> "Topic1".equals(x.getDisplayName())).findAny().orElse(null);
        assertThat(result1).isNotNull();
        assertThat(result1.getId()).isEqualTo(1);
        Topic result2 = responseTopicAsList.stream().filter(x -> "Topic2".equals(x.getDisplayName())).findAny().orElse(null);
        assertThat(result2).isNotNull();
        assertThat(result2.getId()).isEqualTo(2);

    }

    private List<ParentTopic> getTopics() {
        Set<Topic> topics = new HashSet<Topic>() {{
            add(new Topic(1, "Topic1"));
            add(new Topic(2, "Topic2"));
        }};

        return new ArrayList<ParentTopic>() {{
            add(new ParentTopic(1,"Parent Topic 1", topics));
        }};
    }
}