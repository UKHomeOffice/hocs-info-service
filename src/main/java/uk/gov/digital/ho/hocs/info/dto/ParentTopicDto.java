package uk.gov.digital.ho.hocs.info.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.digital.ho.hocs.info.entities.ParentTopic;

import java.util.UUID;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class ParentTopicDto {

    @JsonProperty("label")
    private String displayName;

    @JsonProperty("value")
    private UUID uuid;

    public static ParentTopicDto from (ParentTopic parentTopic) {
        return new ParentTopicDto(parentTopic.getDisplayName(), parentTopic.getUuid()); }
}
