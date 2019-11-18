package uk.gov.digital.ho.hocs.info.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.digital.ho.hocs.info.domain.model.Template;

import java.util.UUID;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class TemplateDto {

    @JsonProperty("label")
    private String displayName;

    @JsonProperty("value")
    private UUID uuid;

    @JsonProperty("caseType")
    private String caseType;

    @JsonProperty("documentUUID")
    private UUID documentUUID;

    public static TemplateDto from (Template template) {
        return new TemplateDto(template.getDisplayName(), template.getUuid(), template.getCaseType(), template.getDocumentUUID()); }
}
