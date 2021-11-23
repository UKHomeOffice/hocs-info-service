package uk.gov.digital.ho.hocs.info.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import uk.gov.digital.ho.hocs.info.domain.model.Topic;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class GetTopicsResponse {

    @JsonProperty("topics")
    List<TopicDto> topics;

    public static GetTopicsResponse from(List<Topic> topics) {
        List<TopicDto> topicDto = topics.stream().map(TopicDto::from).collect(Collectors.toList());
        return new GetTopicsResponse(topicDto);
    }
}
