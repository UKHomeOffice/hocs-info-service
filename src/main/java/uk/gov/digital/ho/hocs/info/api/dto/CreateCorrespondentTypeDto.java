package uk.gov.digital.ho.hocs.info.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CreateCorrespondentTypeDto {

    @JsonProperty("displayName")
    private String displayName;

    @JsonProperty("type")
    private String type;

}
