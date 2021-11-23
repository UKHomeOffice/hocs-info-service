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
public class GetCorrespondentResponse {

    @JsonProperty("displayName")
    private String displayName;

    @JsonProperty("uuid")
    private UUID uuid;

    public static GetCorrespondentResponse from(StandardLine standardLine) {
        return new GetCorrespondentResponse(standardLine.getDisplayName(), standardLine.getUuid());
    }
}
