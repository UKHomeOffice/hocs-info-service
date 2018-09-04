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

    @JsonProperty("documentKey")
    private String documentKey;

    public static GetTemplateResponse from (Template template) {
        return new GetTemplateResponse( template.getDocumentKey());
    }
}
