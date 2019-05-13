package uk.gov.digital.ho.hocs.info.client.documentClient;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uk.gov.digital.ho.hocs.info.application.RestHelper;
import uk.gov.digital.ho.hocs.info.client.documentClient.dto.CreateManagedDocumentRequest;
import uk.gov.digital.ho.hocs.info.client.documentClient.model.ManagedDocumentType;

import java.util.UUID;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DocumentClientTest {

    @Mock
    RestHelper restHelper;

    private final String serviceBaseURL = "http://localhost:8087";
    private final UUID uuid = UUID.randomUUID();
    private final UUID SL_EXT_REF = UUID.fromString("77777777-7777-7777-7777-777777777777");
    private final String DISPLAY_NAME = "DN";
    private final String FILE_LOC = "ANY.txt";


    DocumentClient documentClient;

    @Before
    public void setUp() {
        this.documentClient = new DocumentClient(restHelper, serviceBaseURL);
    }

    @Test
    public void shouldCreateDocument() {
        UUID createManagedDocumentResponse = UUID.randomUUID();

        when(restHelper.post(eq(serviceBaseURL), eq("/document"), any(CreateManagedDocumentRequest.class), eq(UUID.class))).thenReturn(new ResponseEntity<>(createManagedDocumentResponse, HttpStatus.OK));

        documentClient.createDocument(SL_EXT_REF, DISPLAY_NAME, FILE_LOC,  ManagedDocumentType.STANDARD_LINE);

        verify(restHelper, times(1)).post(eq(serviceBaseURL), eq("/document"), any(CreateManagedDocumentRequest.class), eq(UUID.class));
        verifyNoMoreInteractions(restHelper);

    }

    @Test
    public void shouldDeleteDocument() {

        when(restHelper.delete(serviceBaseURL, String.format("/document/%s", uuid), Void.class)).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        documentClient.deleteDocument(uuid);

        verify(restHelper, times(1)).delete(serviceBaseURL, String.format("/document/%s", uuid),  Void.class);
        verifyNoMoreInteractions(restHelper);

    }
}