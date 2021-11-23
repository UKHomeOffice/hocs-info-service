package uk.gov.digital.ho.hocs.info.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class AddTeamToTopicDto {

    @JsonProperty("case_type")
    private String caseType;

    @JsonProperty("stage_type")
    private String stageType;
}
