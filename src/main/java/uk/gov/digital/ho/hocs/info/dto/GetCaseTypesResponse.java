package uk.gov.digital.ho.hocs.info.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class GetCaseTypesResponse {

    @JsonProperty("caseTypes")
    List<CaseTypeDto> caseTypes;

    public static GetCaseTypesResponse from(List<CaseTypeDto> caseTypes) {
        return new GetCaseTypesResponse(caseTypes);
    }
}
