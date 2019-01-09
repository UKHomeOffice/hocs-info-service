package uk.gov.digital.ho.hocs.info.api.dto;

import org.junit.Test;
import uk.gov.digital.ho.hocs.info.domain.model.ParentTopic;
import uk.gov.digital.ho.hocs.info.domain.model.Topic;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

public class GetParentTopicsResponseTest {

    UUID topicUUID = UUID.randomUUID();
    UUID parentTopicUUID1 = UUID.randomUUID();
    UUID parentTopicUUID2 = UUID.randomUUID();

    @Test
    public void from() {
        Set<Topic> topics = new HashSet<>();
        topics.add(new Topic(1l, "Topic Display Name", topicUUID));
        List<ParentTopic> parentTopics = new ArrayList<>();
        parentTopics.add(new ParentTopic(1l,parentTopicUUID1,  "Display Name 1", UUID.randomUUID(), UUID.randomUUID(), topics ));
        parentTopics.add(new ParentTopic(2l, parentTopicUUID2, "Display Name 2",  UUID.randomUUID(), UUID.randomUUID(),topics ));

        GetParentTopicsResponse getParentTopicsResponse = GetParentTopicsResponse.from(parentTopics);

        List<ParentTopicDto> responseAsList = new ArrayList<>(Objects.requireNonNull(getParentTopicsResponse.getParentTopicDto()));

        ParentTopicDto result1 = responseAsList.stream().filter(x -> "Display Name 1".equals(x.getDisplayName())).findAny().orElse(null);
        assertThat(result1).isNotNull();
        assertThat(result1.getUuid()).isEqualTo(parentTopicUUID1);
        ParentTopicDto result2 = responseAsList.stream().filter(x -> "Display Name 2".equals(x.getDisplayName())).findAny().orElse(null);
        assertThat(result2).isNotNull();
        assertThat(result2.getUuid()).isEqualTo(parentTopicUUID2);

    }
}