package uk.gov.digital.ho.hocs.info.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.digital.ho.hocs.info.entities.CorrespondentType;

@AllArgsConstructor
@Getter
public class CorrespondentTypeDto {

    @JsonProperty("label")
    private String displayName;

    @JsonProperty("value")
    private String type;

    public static CorrespondentTypeDto from(CorrespondentType correspondentType){
        return new CorrespondentTypeDto(correspondentType.getDisplayName(), correspondentType.getType());
    }

}

