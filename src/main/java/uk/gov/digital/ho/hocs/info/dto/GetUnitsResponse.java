package uk.gov.digital.ho.hocs.info.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import uk.gov.digital.ho.hocs.info.entities.Unit;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class GetUnitsResponse {

    @JsonProperty("teams")
    List<Unit> units;

    public static GetUnitsResponse from(final List<Unit> units) {
        return new GetUnitsResponse(units);
    }
}