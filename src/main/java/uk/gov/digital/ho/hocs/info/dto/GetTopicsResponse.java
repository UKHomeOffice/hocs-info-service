package uk.gov.digital.ho.hocs.info.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import uk.gov.digital.ho.hocs.info.entities.ParentTopic;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class GetTopicsResponse {

    @JsonProperty("topics")
    List<ParentTopic> topics;

    public static GetTopicsResponse from(List<ParentTopic> topics) {
        return new GetTopicsResponse(topics);
    }
}
