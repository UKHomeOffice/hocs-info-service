package uk.gov.digital.ho.hocs.info.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import uk.gov.digital.ho.hocs.info.entities.Template;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class GetTemplateKeyResponse {

    @JsonProperty("documentKey")
    private String documentKey;

    public static GetTemplateKeyResponse from (Template template) {
        return new GetTemplateKeyResponse( template.getDocumentKey());
    }
}
