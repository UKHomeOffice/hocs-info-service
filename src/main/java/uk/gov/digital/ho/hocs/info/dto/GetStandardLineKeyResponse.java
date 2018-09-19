package uk.gov.digital.ho.hocs.info.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import uk.gov.digital.ho.hocs.info.entities.StandardLine;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class GetStandardLineKeyResponse {

    @JsonProperty("documentKey")
    private String documentKey;

    public static GetStandardLineKeyResponse from (StandardLine standardLine) {
        return new GetStandardLineKeyResponse( standardLine.getDocumentKey());
    }
}
