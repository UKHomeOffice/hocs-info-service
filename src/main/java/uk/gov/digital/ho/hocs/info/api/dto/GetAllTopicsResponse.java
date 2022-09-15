package uk.gov.digital.ho.hocs.info.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import uk.gov.digital.ho.hocs.info.domain.model.ParentTopic;
import uk.gov.digital.ho.hocs.info.domain.model.Topic;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class GetAllTopicsResponse {

    @JsonProperty("parentTopics")
    List<ParentTopicAndTopicDto> parentTopicAndTopicDtos;

    public static GetAllTopicsResponse from(List<ParentTopic> parentTopics) {
        List<ParentTopicAndTopicDto> parentTopicAndTopicDtos = parentTopics.stream().map(
            ParentTopicAndTopicDto::from).collect(Collectors.toList());
        return new GetAllTopicsResponse(parentTopicAndTopicDtos);
    }

    public static GetAllTopicsResponse fromTopicWithFilteredChildren(List<ParentTopic> parentTopics,
                                                                     List<Topic> filteredTopics) {
        List<ParentTopicAndTopicDto> parentTopicAndTopicDtos = parentTopics.stream().map(
            pt -> ParentTopicAndTopicDto.fromTopicWithFilteredChildren(pt, filteredTopics)).collect(
            Collectors.toList());
        return new GetAllTopicsResponse(parentTopicAndTopicDtos);
    }

}
