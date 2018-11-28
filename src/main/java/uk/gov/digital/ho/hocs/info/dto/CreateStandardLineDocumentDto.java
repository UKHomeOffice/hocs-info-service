package uk.gov.digital.ho.hocs.info.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CreateStandardLineDocumentDto {

    @JsonProperty("displayName")
    private String displayName;

    @JsonProperty("s3UntrustedUrl")
    private String s3UntrustedUrl;

    @JsonProperty("topicUUID")
    private UUID topicUUID;

    @JsonProperty("expires")
    private LocalDate expires;
}
