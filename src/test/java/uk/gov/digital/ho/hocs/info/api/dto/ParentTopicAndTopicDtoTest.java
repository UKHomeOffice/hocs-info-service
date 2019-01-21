package uk.gov.digital.ho.hocs.info.api.dto;

import org.junit.Test;
import uk.gov.digital.ho.hocs.info.domain.model.ParentTopic;
import uk.gov.digital.ho.hocs.info.domain.model.Topic;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

public class ParentTopicAndTopicDtoTest {

    UUID topicUUID = UUID.randomUUID();
    UUID parentTopicUUID = UUID.randomUUID();


    @Test
    public void from() {
        Set<Topic> topics = new HashSet<>();
        topics.add(new Topic(1l,"Topic Display Name", topicUUID));
        ParentTopic parentTopic = new ParentTopic(1l, parentTopicUUID,"Parent Topic Display Name", topics);

        ParentTopicAndTopicDto parentTopicAndTopicDto = ParentTopicAndTopicDto.from(parentTopic);

        assertThat(parentTopicAndTopicDto.getDisplayName()).isEqualTo("Parent Topic Display Name");
        assertThat(parentTopicAndTopicDto.getUuid()).isEqualTo(parentTopicUUID);
        assertThat(parentTopicAndTopicDto.getTopics().get(0).getDisplayName()).isEqualTo("Topic Display Name");
        assertThat(parentTopicAndTopicDto.getTopics().get(0).getUuid()).isEqualTo(topicUUID);


    }
}