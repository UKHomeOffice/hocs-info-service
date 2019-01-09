package uk.gov.digital.ho.hocs.info.client.documentClient.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CreateManagedDocumentResponse {

    @JsonProperty("uuid")
    private UUID uuid;

}
