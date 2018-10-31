package uk.gov.digital.ho.hocs.info.dto;

import org.junit.Test;
import uk.gov.digital.ho.hocs.info.entities.ParentTopic;
import uk.gov.digital.ho.hocs.info.entities.Topic;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

public class GetAllTopicsResponseTest {

    UUID uuid = UUID.randomUUID();

    @Test
    public void from() {
        Set<Topic> topicSet = new HashSet<>();
        topicSet.add(new Topic(1l,"Topic1",uuid));
        List<ParentTopic> parentTopics = new ArrayList<>();
        parentTopics.add(new ParentTopic(1l,"ParentTopic",uuid,topicSet));

        GetAllTopicsResponse getAllTopicsResponse = GetAllTopicsResponse.from(parentTopics);

        assertThat(getAllTopicsResponse.getParentTopicAndTopicDtos()).isNotEmpty();
        assertThat(getAllTopicsResponse.getParentTopicAndTopicDtos().get(0).getDisplayName()).isEqualTo("ParentTopic");
        assertThat(getAllTopicsResponse.getParentTopicAndTopicDtos().get(0).getUuid()).isEqualTo(uuid);
        assertThat(getAllTopicsResponse.getParentTopicAndTopicDtos().get(0).getTopics().get(0).getDisplayName()).isEqualTo("Topic1");
        assertThat(getAllTopicsResponse.getParentTopicAndTopicDtos().get(0).getTopics().get(0).getUuid()).isEqualTo(uuid);



    }
}