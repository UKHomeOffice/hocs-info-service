package uk.gov.digital.ho.hocs.info.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import uk.gov.digital.ho.hocs.info.entities.ParentTopic;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class GetAllTopicsResponse {

    @JsonProperty("parentTopics")
    List<ParentTopicAndTopicDto> parentTopicAndTopicDtos;

    public static GetAllTopicsResponse from(List<ParentTopic> parentTopics) {
        List<ParentTopicAndTopicDto> parentTopicAndTopicDtos = parentTopics.stream().map(ParentTopicAndTopicDto::from).collect(Collectors.toList());
        return new GetAllTopicsResponse(parentTopicAndTopicDtos);
    }
}
