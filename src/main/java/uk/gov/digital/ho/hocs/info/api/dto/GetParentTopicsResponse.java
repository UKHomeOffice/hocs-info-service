package uk.gov.digital.ho.hocs.info.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import uk.gov.digital.ho.hocs.info.domain.model.ParentTopic;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class GetParentTopicsResponse {

    @JsonProperty("parentTopics")
    List<ParentTopicDto> parentTopicDto;

    public static GetParentTopicsResponse from(List<ParentTopic> parentTopics) {
        List<ParentTopicDto> parentTopicDto = parentTopics.stream().map(ParentTopicDto::from).collect(
            Collectors.toList());
        return new GetParentTopicsResponse(parentTopicDto);
    }

}
