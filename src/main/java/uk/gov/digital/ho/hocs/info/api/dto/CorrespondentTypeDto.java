package uk.gov.digital.ho.hocs.info.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.digital.ho.hocs.info.domain.model.CorrespondentType;

@AllArgsConstructor
@Getter
public class CorrespondentTypeDto {

    @JsonProperty("label")
    private String displayName;

    @JsonProperty("value")
    private String type;

    public static CorrespondentTypeDto from(CorrespondentType correspondentType) {
        return new CorrespondentTypeDto(correspondentType.getDisplayName(), correspondentType.getType());
    }

}

