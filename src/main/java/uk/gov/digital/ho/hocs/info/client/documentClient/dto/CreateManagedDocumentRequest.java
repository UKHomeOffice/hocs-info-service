package uk.gov.digital.ho.hocs.info.client.documentClient.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.digital.ho.hocs.info.client.documentClient.model.ManagedDocumentType;

import java.util.UUID;

@AllArgsConstructor
@Getter
public class CreateManagedDocumentRequest {

    @JsonProperty("name")
    private String name;

    @JsonProperty("type")
    private ManagedDocumentType type;

    @JsonProperty("externalReferenceUuid")
    private UUID externalReferenceUuid;

}