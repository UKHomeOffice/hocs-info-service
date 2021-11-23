package uk.gov.digital.ho.hocs.info.client.documentclient.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.digital.ho.hocs.info.client.documentclient.model.ManagedDocumentType;

import java.util.UUID;

@AllArgsConstructor
@Getter
public class CreateManagedDocumentRequest {

    @JsonProperty("name")
    private String name;

    @JsonProperty("type")
    private ManagedDocumentType type;

    @JsonProperty("fileLink")
    private String fileLink;

    @JsonProperty("externalReferenceUUID")
    private UUID externalReferenceUuid;

}