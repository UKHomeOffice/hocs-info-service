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
public class TeamDeleteActiveParentTopicsDto {

    @JsonProperty("")
    String errorMessage;

    @JsonProperty("parentTopics")
    List<ParentTopicDto> parentTopicDto;

    public static TeamDeleteActiveParentTopicsDto from(List<ParentTopic> parentTopics, String errorMessage) {
        List<ParentTopicDto> parentTopicDto = parentTopics.stream().map(ParentTopicDto::from).collect(Collectors.toList());
        return new TeamDeleteActiveParentTopicsDto(errorMessage,parentTopicDto);
    }
}
