package uk.gov.digital.ho.hocs.info.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import uk.gov.digital.ho.hocs.info.entities.Unit;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class GetUnitsResponse {

    @JsonProperty("units")
    Set<UnitDto> units;

    public static GetUnitsResponse from(final Set<Unit> units) {
        Set<UnitDto> unitDtos = units.stream().map(UnitDto::from).collect(Collectors.toSet());
        return new GetUnitsResponse(unitDtos);
    }
}