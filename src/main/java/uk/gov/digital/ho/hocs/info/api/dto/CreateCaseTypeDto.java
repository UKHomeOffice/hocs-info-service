package uk.gov.digital.ho.hocs.info.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.digital.ho.hocs.info.domain.model.CaseType;

@AllArgsConstructor
@Getter
public class CreateCaseTypeDto {

//    @JsonProperty("label")
//    private String label;
//
//    @JsonProperty("value")
//    private String value;

    @JsonProperty("displayName")
    private String displayName;

    @JsonProperty("shortCode")
    private String shortCode;

    @JsonProperty("type")
    private String type;

    @JsonProperty("active")
    private boolean active;

    @JsonProperty("bulk")
    private boolean bulk;

    @JsonProperty("deadlineStage")
    private String deadlineStage;

}
