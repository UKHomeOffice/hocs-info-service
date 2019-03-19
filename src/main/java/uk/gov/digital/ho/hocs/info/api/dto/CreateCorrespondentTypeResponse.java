package uk.gov.digital.ho.hocs.info.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.digital.ho.hocs.info.domain.model.CorrespondentType;

import java.util.UUID;

@AllArgsConstructor
@Getter
public class CreateCorrespondentTypeResponse {

    @JsonProperty("uuid")
    private UUID uuid;

    @JsonProperty("displayName")
    private String displayName;

    @JsonProperty("type")
    private String type;

    public static CreateCorrespondentTypeResponse from(CorrespondentType correspondentType) {
        return new CreateCorrespondentTypeResponse(correspondentType.getUuid(), correspondentType.getDisplayName(), correspondentType.getType());
    }
}

