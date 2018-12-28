package uk.gov.digital.ho.hocs.info.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.digital.ho.hocs.info.domain.model.StageTypeEntity;

import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class GetStageTypesResponse {

    @JsonProperty("stageTypes")
    Set<StageTypeDto> stageTypes;

    public static GetStageTypesResponse from (Set<StageTypeEntity> stageTypeSet) {
        Set<StageTypeDto> stageTypes = stageTypeSet.stream().map(StageTypeDto::from).collect(Collectors.toSet());
        return new GetStageTypesResponse(stageTypes);
    }
}
