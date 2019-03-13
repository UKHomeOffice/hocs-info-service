package uk.gov.digital.ho.hocs.info.api.dto;

import org.junit.Test;
import uk.gov.digital.ho.hocs.info.domain.model.Topic;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class GetTopicsResponseTest {

    UUID uuid1 = UUID.randomUUID();
    UUID uuid2 = UUID.randomUUID();

    @Test
    public void from() {
        List<Topic> topics = new ArrayList<>();
        topics.add(new Topic(1l, "Topic 1", uuid1, uuid2, true));
        topics.add(new Topic(2l, "Topic 2", uuid1, uuid2, true));

        GetTopicsResponse getTopicsResponse = GetTopicsResponse.from(topics);

        List<TopicDto> responseAsList = new ArrayList<>(Objects.requireNonNull(getTopicsResponse.getTopics()));

        TopicDto result1 = responseAsList.stream().filter(x -> "Topic 1".equals(x.getDisplayName())).findAny().orElse(null);
        assertThat(result1).isNotNull();
        assertThat(result1.getUuid()).isEqualTo(uuid1);
        TopicDto result2 = responseAsList.stream().filter(x -> "Topic 2".equals(x.getDisplayName())).findAny().orElse(null);
        assertThat(result2).isNotNull();
        assertThat(result2.getUuid()).isEqualTo(uuid2);

    }
}