package uk.gov.digital.ho.hocs.info.client.documentClient;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import uk.gov.digital.ho.hocs.info.application.RestHelper;
import uk.gov.digital.ho.hocs.info.client.documentClient.dto.CreateManagedDocumentRequest;
import uk.gov.digital.ho.hocs.info.client.documentClient.model.ManagedDocumentType;
import uk.gov.digital.ho.hocs.info.domain.exception.ApplicationExceptions;

import java.util.UUID;

@Slf4j
@Component
public class DocumentClient {

    private final RestHelper restHelper;
    private final String serviceBaseURL;

    @Autowired
    public DocumentClient(RestHelper restHelper,
                          @Value("${hocs.document-service}") String documentService) {
        this.restHelper = restHelper;
        this.serviceBaseURL = documentService;
    }

    public void createDocument(UUID referenceUUID, String displayName, String fileLocation, ManagedDocumentType type) {
        CreateManagedDocumentRequest request = new CreateManagedDocumentRequest(displayName, type, fileLocation, referenceUUID);
        ResponseEntity<UUID> response = restHelper.post(serviceBaseURL, "/document", request, UUID.class);
        if (response.getStatusCodeValue() == 200) {
            log.info("Created Managed Document {}, Reference {}", response, referenceUUID);
        } else {
            throw new ApplicationExceptions.EntityCreationException("Could not create Managed Document; response: %s", response.getStatusCodeValue());
        }
    }

    public void deleteDocument(UUID documentUUID) {
        ResponseEntity<Void> response = restHelper.delete(serviceBaseURL, String.format("/document/%s", documentUUID), Void.class);

        if(response.getStatusCodeValue() == 200) {
            log.info("Deleted Document {}", documentUUID);
        } else {
            throw new ApplicationExceptions.EntityCreationException("Could not delete Document; response: %s", response.getStatusCodeValue());
        }
    }
}
