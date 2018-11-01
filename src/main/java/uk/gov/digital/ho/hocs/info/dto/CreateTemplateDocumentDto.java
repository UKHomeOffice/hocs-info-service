package uk.gov.digital.ho.hocs.info.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import uk.gov.digital.ho.hocs.info.documentClient.model.ManagedDocumentType;

import java.time.LocalDate;
import java.util.UUID;

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
