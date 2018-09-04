package uk.gov.digital.ho.hocs.info.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import uk.gov.digital.ho.hocs.info.entities.StandardLines;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class GetStandardLinesResponse {

    @JsonProperty("displayName")
    private String displayName;

    @JsonProperty("documentkey")
    private String documentkey;

    public static GetStandardLinesResponse from(StandardLines standardLines) {
        return new GetStandardLinesResponse(standardLines.getDisplayName(), standardLines.getDocumentKey());
    }
}
