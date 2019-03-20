package uk.gov.digital.ho.hocs.info.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import uk.gov.digital.ho.hocs.info.domain.model.CorrespondentType;

import java.util.Set;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class GetCorrespondentTypeResponse {

    @JsonProperty("correspondentTypes")
    Set<CorrespondentTypeDto> correspondentTypes;

    public static GetCorrespondentTypeResponse from(Set<CorrespondentType> correspondentTypeSet) {
        Set<CorrespondentTypeDto> correspondentTypes = correspondentTypeSet.stream().map(CorrespondentTypeDto::from).collect(Collectors.toSet());
        return new GetCorrespondentTypeResponse(correspondentTypes);
    }
}
