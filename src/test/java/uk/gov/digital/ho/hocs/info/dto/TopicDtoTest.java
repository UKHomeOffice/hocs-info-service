package uk.gov.digital.ho.hocs.info.dto;

import org.junit.Test;
import uk.gov.digital.ho.hocs.info.entities.Template;
import uk.gov.digital.ho.hocs.info.entities.Topic;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class TopicDtoTest {

    UUID uuid = UUID.randomUUID();

    @Test
    public void from() {
        Topic topic = new Topic(1l,"DisplayName",  uuid);

        TopicDto topicDto = TopicDto.from(topic);

        assertThat(topicDto.getDisplayName()).isEqualTo("DisplayName");
        assertThat(topicDto.getUuid()).isEqualTo(uuid);

    }
}