package uk.gov.digital.ho.hocs.info.documentClient.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@Getter
public class CreateManagedDocumentResponse {

    @JsonProperty("uuid")
    private UUID uuid;

}
