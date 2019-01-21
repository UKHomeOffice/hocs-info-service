package uk.gov.digital.ho.hocs.info.api.dto;

import org.junit.Test;
import uk.gov.digital.ho.hocs.info.domain.model.ParentTopic;
import uk.gov.digital.ho.hocs.info.domain.model.Topic;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

public class GetAllTopicsResponseTest {

    UUID uuid = UUID.randomUUID();

    @Test
    public void from() {
        Set<Topic> topicSet = new HashSet<>();
        topicSet.add(new Topic(1l,"Topic1",uuid));
        List<ParentTopic> parentTopics = new ArrayList<>();
        parentTopics.add(new ParentTopic(1l,uuid, "ParentTopic", topicSet));

        GetAllTopicsResponse getAllTopicsResponse = GetAllTopicsResponse.from(parentTopics);

        assertThat(getAllTopicsResponse.getParentTopicAndTopicDtos()).isNotEmpty();
        assertThat(getAllTopicsResponse.getParentTopicAndTopicDtos().get(0).getDisplayName()).isEqualTo("ParentTopic");
        assertThat(getAllTopicsResponse.getParentTopicAndTopicDtos().get(0).getUuid()).isEqualTo(uuid);
        assertThat(getAllTopicsResponse.getParentTopicAndTopicDtos().get(0).getTopics().get(0).getDisplayName()).isEqualTo("Topic1");
        assertThat(getAllTopicsResponse.getParentTopicAndTopicDtos().get(0).getTopics().get(0).getUuid()).isEqualTo(uuid);



    }
}