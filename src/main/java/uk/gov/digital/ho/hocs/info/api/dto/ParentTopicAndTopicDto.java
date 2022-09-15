package uk.gov.digital.ho.hocs.info.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.digital.ho.hocs.info.domain.model.ParentTopic;
import uk.gov.digital.ho.hocs.info.domain.model.Topic;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class ParentTopicAndTopicDto {

    @JsonProperty("label")
    private String displayName;

    @JsonProperty("value")
    private UUID uuid;

    @JsonProperty("options")
    List<TopicDto> topics;

    public static ParentTopicAndTopicDto from(ParentTopic parentTopic) {
        List<TopicDto> topicDto = parentTopic.getTopic().stream().map(TopicDto::from).collect(Collectors.toList());
        return new ParentTopicAndTopicDto(parentTopic.getDisplayName(), parentTopic.getUuid(), topicDto);
    }

    public static ParentTopicAndTopicDto fromTopicWithFilteredChildren(ParentTopic parentTopic,
                                                                       List<Topic> childTopics) {
        List<TopicDto> filteredChildTopics = childTopics.stream().filter(
            t -> t.getParentTopic().equals(parentTopic.getUuid())).map(TopicDto::from).collect(Collectors.toList());
        return new ParentTopicAndTopicDto(parentTopic.getDisplayName(), parentTopic.getUuid(), filteredChildTopics);
    }

}
