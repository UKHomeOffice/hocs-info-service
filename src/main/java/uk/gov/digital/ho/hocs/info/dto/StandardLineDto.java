package uk.gov.digital.ho.hocs.info.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import uk.gov.digital.ho.hocs.info.entities.StandardLines;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class StandardLineDto {

    @JsonProperty("displayName")
    private String displayName;

    @JsonProperty("documentkey")
    private String documentkey;

    public static StandardLineDto from(StandardLines standardLines) {
        return new StandardLineDto(standardLines.getDisplayName(), standardLines.getDocumentKey());
    }
}
