package uk.gov.digital.ho.hocs.info.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CreateTemplateDocumentDto {

    @JsonProperty("displayName")
    private String displayName;

    @JsonProperty("caseType")
    private String caseType;

    @JsonProperty("s3UntrustedUrl")
    private String s3UntrustedUrl;
}
