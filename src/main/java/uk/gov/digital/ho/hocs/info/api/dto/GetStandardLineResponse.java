package uk.gov.digital.ho.hocs.info.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import uk.gov.digital.ho.hocs.info.domain.model.StandardLine;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class GetStandardLineResponse {

    @JsonProperty("displayName")
    private String displayName;

    @JsonProperty("uuid")
    private UUID uuid;

    public static GetStandardLineResponse from(StandardLine standardLine) {
        return new GetStandardLineResponse(standardLine.getDisplayName(), standardLine.getUuid());
    }
}
