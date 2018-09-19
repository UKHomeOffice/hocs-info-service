package uk.gov.digital.ho.hocs.info.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import uk.gov.digital.ho.hocs.info.entities.ParentTopic;
import uk.gov.digital.ho.hocs.info.entities.StandardLines;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class GetStandardLinesResponse {

    @JsonProperty("standardLine")
    List<StandardLineDto> standardLineDtos;

    public static GetStandardLinesResponse from(List<StandardLines> standardLines) {
        List<StandardLineDto> standardLineDtos = standardLines.stream().map(StandardLineDto::from).collect(Collectors.toList());
        return new GetStandardLinesResponse(standardLineDtos);
    }
}
