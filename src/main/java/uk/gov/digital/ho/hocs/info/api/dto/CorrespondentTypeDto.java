package uk.gov.digital.ho.hocs.info.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.digital.ho.hocs.info.domain.model.CorrespondentType;

@AllArgsConstructor
@Getter
public class CorrespondentTypeDto {

    //TODO: remove - used in UI
    @JsonProperty("label")
    private String label;

    //TODO: remove - used in UI
    @JsonProperty("value")
    private String value;

    @JsonProperty("displayName")
    private String displayName;

    @JsonProperty("type")
    private String type;

    public static CorrespondentTypeDto from(CorrespondentType correspondentType) {
        return new CorrespondentTypeDto(
                correspondentType.getDisplayName(),
                correspondentType.getType(),
                correspondentType.getDisplayName(),
                correspondentType.getType());
    }

}

