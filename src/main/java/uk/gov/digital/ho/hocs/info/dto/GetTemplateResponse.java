package uk.gov.digital.ho.hocs.info.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import uk.gov.digital.ho.hocs.info.entities.Template;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class GetTemplateResponse {

    @JsonProperty("displayName")
    private String displayName;

    @JsonProperty("file_link")
    private String fileLink;

    public static GetTemplateResponse from (Template template) {
        return new GetTemplateResponse(template.getDisplayName(), template.getFileLink());
    }
}
