package uk.gov.digital.ho.hocs.info.standardLine;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.internal.matchers.Null;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.digital.ho.hocs.info.documentClient.DocumentClient;
import uk.gov.digital.ho.hocs.info.documentClient.model.ManagedDocumentType;
import uk.gov.digital.ho.hocs.info.dto.CreateStandardLineDocumentDto;
import uk.gov.digital.ho.hocs.info.repositories.StandardLineRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class StandardLineServiceTest {

    @Mock
    private StandardLineRepository standardLineRepository;

    @Mock
    private DocumentClient documentClient;

    private StandardLineService standardLineService;

    private final UUID STANDARD_LINE_EXTERNAL_REFERENCE_UUID = UUID.fromString("77777777-7777-7777-7777-777777777777");
    UUID uuid = UUID.randomUUID();
    UUID documentUUID = UUID.randomUUID();
    LocalDateTime endOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
    LocalDateTime localDateTimeNow = LocalDateTime.now();



    @Before
    public void setUp() {
        this.standardLineService = new StandardLineService(standardLineRepository, documentClient);
    }

    @Test
    public void shouldReturnStandardLineForPrimaryTopic(){

        standardLineService.getStandardLines(uuid);
        verify(standardLineRepository, times(1)).findStandardLinesByTopicAndExpires(uuid, endOfDay);
        verifyNoMoreInteractions(standardLineRepository);
    }

    @Test
    public void shouldCreateNewStandardLineTopic(){
        CreateStandardLineDocumentDto request = new CreateStandardLineDocumentDto("dn","url",uuid,LocalDate.now().plusDays(1));

        when(documentClient.createDocument(STANDARD_LINE_EXTERNAL_REFERENCE_UUID, request.getDisplayName(), ManagedDocumentType.STANDARD_LINE)).thenReturn(documentUUID);
        when(standardLineRepository.findStandardLinesByTopicAndExpires(uuid ,endOfDay)).thenReturn(null);

        standardLineService.createStandardLineDocument(request);


        verify(documentClient,times(1)).createDocument(STANDARD_LINE_EXTERNAL_REFERENCE_UUID, "dn",ManagedDocumentType.STANDARD_LINE );
        verify(standardLineRepository, times(1)).findStandardLinesByTopicAndExpires(uuid, endOfDay);
        verify(standardLineRepository, times(1)).save(any());
        verify(documentClient).processDocument(ManagedDocumentType.STANDARD_LINE, documentUUID, "url");
        verifyNoMoreInteractions(standardLineRepository);
        verifyNoMoreInteractions(documentClient);
    }
}
