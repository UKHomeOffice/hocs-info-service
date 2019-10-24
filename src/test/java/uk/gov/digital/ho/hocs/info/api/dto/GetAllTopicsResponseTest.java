package uk.gov.digital.ho.hocs.info.api.dto;

import org.junit.Test;
import uk.gov.digital.ho.hocs.info.domain.model.ParentTopic;
import uk.gov.digital.ho.hocs.info.domain.model.Topic;

import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class GetAllTopicsResponseTest {

    UUID topic1Uuid = UUID.randomUUID();
    UUID topic2Uuid = UUID.randomUUID();
    UUID topic3Uuid = UUID.randomUUID();
    UUID topic4Uuid = UUID.randomUUID();
    UUID parentTopic1Uuid = UUID.randomUUID();
    UUID parentTopic2Uuid = UUID.randomUUID();

    @Test
    public void from() {
        Set<Topic> topicSet = new HashSet<>();
        topicSet.add(new Topic(1l,"Topic1",topic1Uuid, UUID.randomUUID(), true));
        List<ParentTopic> parentTopics = new ArrayList<>();
        parentTopics.add(new ParentTopic(1l, parentTopic1Uuid, "ParentTopic", topicSet, true));

        GetAllTopicsResponse getAllTopicsResponse = GetAllTopicsResponse.from(parentTopics);

        assertThat(getAllTopicsResponse.getParentTopicAndTopicDtos()).isNotEmpty();
        assertThat(getAllTopicsResponse.getParentTopicAndTopicDtos().get(0).getDisplayName()).isEqualTo("ParentTopic");
        assertThat(getAllTopicsResponse.getParentTopicAndTopicDtos().get(0).getUuid()).isEqualTo(topic1Uuid);
        assertThat(getAllTopicsResponse.getParentTopicAndTopicDtos().get(0).getTopics().get(0).getDisplayName()).isEqualTo("Topic1");
        assertThat(getAllTopicsResponse.getParentTopicAndTopicDtos().get(0).getTopics().get(0).getUuid()).isEqualTo(topic1Uuid);
    }

    @Test
    public void fromTopicWithFilteredChildren() {
        Set<Topic> topicSet = new HashSet<>();
        topicSet.add(new Topic(1l,"Topic1", topic1Uuid, parentTopic1Uuid, true));
        topicSet.add(new Topic(1l,"Topic2", topic2Uuid, parentTopic2Uuid, true));
        topicSet.add(new Topic(1l,"Topic3", topic3Uuid, parentTopic1Uuid, true));
        topicSet.add(new Topic(1l,"Topic4", topic4Uuid, parentTopic2Uuid, true));
        List<ParentTopic> parentTopics = new ArrayList<>();
        parentTopics.add(new ParentTopic(1l,parentTopic1Uuid, "ParentTopic1", topicSet, true));
        parentTopics.add(new ParentTopic(1l,parentTopic2Uuid, "ParentTopic2", topicSet, true));

        var filteredTopics = topicSet.stream().filter(t -> Arrays.asList("Topic3", "Topic4").contains(t.getDisplayName())).collect(Collectors.toList());
        GetAllTopicsResponse getAllTopicsResponse = GetAllTopicsResponse.fromTopicWithFilteredChildren(parentTopics, filteredTopics);

        assertThat(getAllTopicsResponse.getParentTopicAndTopicDtos()).isNotEmpty();
        assertThat(getAllTopicsResponse.getParentTopicAndTopicDtos().get(0).getDisplayName()).isEqualTo("ParentTopic1");
        assertThat(getAllTopicsResponse.getParentTopicAndTopicDtos().get(0).getUuid()).isEqualTo(parentTopic1Uuid);
        assertThat(getAllTopicsResponse.getParentTopicAndTopicDtos().get(0).getTopics().get(0).getDisplayName()).isEqualTo("Topic3");
        assertThat(getAllTopicsResponse.getParentTopicAndTopicDtos().get(1).getTopics().get(0).getDisplayName()).isEqualTo("Topic4");
        assertThat(getAllTopicsResponse.getParentTopicAndTopicDtos().get(0).getTopics().get(0).getUuid()).isEqualTo(topic3Uuid);
        assertThat(getAllTopicsResponse.getParentTopicAndTopicDtos().get(1).getTopics().get(0).getUuid()).isEqualTo(topic4Uuid);
        assertThat(getAllTopicsResponse.getParentTopicAndTopicDtos().get(0).getTopics().size()).isEqualTo(1);
        assertThat(getAllTopicsResponse.getParentTopicAndTopicDtos().get(1).getTopics().size()).isEqualTo(1);
    }
}