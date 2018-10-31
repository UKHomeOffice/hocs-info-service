package uk.gov.digital.ho.hocs.info.dto;

import org.junit.Test;
import uk.gov.digital.ho.hocs.info.entities.ParentTopic;

import java.util.HashSet;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class ParentTopicDtoTest {

    UUID uuid = UUID.randomUUID();

    @Test
    public void from() {
        ParentTopic parentTopic = new ParentTopic(1l,"Display Name", uuid, new HashSet<>());

        ParentTopicDto parentTopicDto = ParentTopicDto.from(parentTopic);

        assertThat(parentTopicDto.getDisplayName()).isEqualTo("Display Name");
        assertThat(parentTopicDto.getUuid()).isEqualTo(uuid);
    }
}