package uk.gov.digital.ho.hocs.info.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import uk.gov.digital.ho.hocs.info.domain.model.Template;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class GetTemplateResponse {

    @JsonProperty("displayName")
    private String displayName;

    @JsonProperty("uuid")
    private UUID uuid;

    @JsonProperty("caseType")
    private String caseType;

    @JsonProperty("documentUUID")
    private UUID documentUUID;

    public static GetTemplateResponse from(Template template) {
        return new GetTemplateResponse(template.getDisplayName(), template.getUuid(), template.getCaseType(), template.getDocumentUUID());
    }
}
