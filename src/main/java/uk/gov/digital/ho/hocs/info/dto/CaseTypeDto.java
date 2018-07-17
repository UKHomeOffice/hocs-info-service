package uk.gov.digital.ho.hocs.info.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CaseTypeDto {


    @JsonProperty("requiredRole")
    private String tenant;

    @JsonProperty("label")
    private String displayName;

    @JsonProperty("value")
    private String type;

}
