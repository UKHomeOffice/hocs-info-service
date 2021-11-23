package uk.gov.digital.ho.hocs.info.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CreateCaseTypeDto {

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

    @JsonProperty("previousCaseType")
    private String previousCaseType;
}
