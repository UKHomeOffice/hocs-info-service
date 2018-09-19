package uk.gov.digital.ho.hocs.info.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.digital.ho.hocs.info.entities.Template;
import uk.gov.digital.ho.hocs.info.entities.Topic;

import java.util.UUID;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class TemplateDto {

    @JsonProperty("label")
    private String displayName;

    @JsonProperty("value")
    private UUID uuid;

    public static TemplateDto from (Template template) {
        return new TemplateDto(template.getDisplayName(), template.getUuid()); }
}
