package uk.gov.digital.ho.hocs.info.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.digital.ho.hocs.info.entities.CaseTypeEntity;

import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class GetCaseTypesResponse {

    @JsonProperty("caseTypes")
    Set<CaseTypeDto> caseTypes;

    public static GetCaseTypesResponse from (Set<CaseTypeEntity> caseTypeSet) {
        Set<CaseTypeDto> caseTypes = caseTypeSet.stream().map(CaseTypeDto::from).collect(Collectors.toSet());
        return new GetCaseTypesResponse(caseTypes);
    }
}
