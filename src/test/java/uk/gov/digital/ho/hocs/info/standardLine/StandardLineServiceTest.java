package uk.gov.digital.ho.hocs.info.standardLine;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.digital.ho.hocs.info.caseworkclient.CaseworkClient;
import uk.gov.digital.ho.hocs.info.documentClient.DocumentClient;
import uk.gov.digital.ho.hocs.info.documentClient.model.ManagedDocumentType;
import uk.gov.digital.ho.hocs.info.dto.CreateStandardLineDocumentDto;
import uk.gov.digital.ho.hocs.info.entities.StandardLine;
import uk.gov.digital.ho.hocs.info.exception.EntityCreationException;
import uk.gov.digital.ho.hocs.info.exception.EntityNotFoundException;
import uk.gov.digital.ho.hocs.info.repositories.StandardLineRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class StandardLineServiceTest {

    @Mock
    private StandardLineRepository standardLineRepository;

    @Mock
    private DocumentClient documentClient;

    @Mock
    private CaseworkClient caseworkClient;

    private StandardLineService standardLineService;

    private static final UUID SL_EXT_REF = UUID.fromString("77777777-7777-7777-7777-777777777777");
    private static final UUID uuid = UUID.randomUUID();
    private static final UUID DOCUMENT_UUID = UUID.randomUUID();
    private static final UUID NEW_DOCUMENT_UUID = UUID.randomUUID();
    private static final LocalDateTime END_OF_DAY = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
    private static final String DISPLAY_NAME = "dn";

    @Before
    public void setUp() {
        this.standardLineService = new StandardLineService(standardLineRepository, documentClient, caseworkClient);
    }

    @Test
    public void shouldReturnStandardLineForPrimaryTopic(){
        when(standardLineRepository.findStandardLinesByTopicAndExpires(uuid, END_OF_DAY)).thenReturn(new StandardLine());
        standardLineService.getStandardLines(uuid);
        verify(standardLineRepository, times(1)).findStandardLinesByTopicAndExpires(uuid, END_OF_DAY);
        verifyNoMoreInteractions(standardLineRepository);
    }

    @Test
    public void shouldCreateNewStandardLine(){
        CreateStandardLineDocumentDto request = new CreateStandardLineDocumentDto(DISPLAY_NAME,"url",uuid,LocalDate.now().plusDays(1));

        when(documentClient.createDocument(SL_EXT_REF, request.getDisplayName(), ManagedDocumentType.STANDARD_LINE)).thenReturn(DOCUMENT_UUID);
        when(standardLineRepository.findStandardLinesByTopicAndExpires(uuid , END_OF_DAY)).thenReturn(null);

        standardLineService.createStandardLineDocument(request);

        verify(documentClient,times(1)).createDocument(SL_EXT_REF, DISPLAY_NAME,ManagedDocumentType.STANDARD_LINE );
        verify(standardLineRepository, times(1)).findStandardLinesByTopicAndExpires(uuid, END_OF_DAY);
        verify(standardLineRepository, times(1)).save(any());
        verify(documentClient).processDocument(ManagedDocumentType.STANDARD_LINE, DOCUMENT_UUID, "url");
        verifyNoMoreInteractions(standardLineRepository);
        verifyNoMoreInteractions(documentClient);
    }

    @Test
    public void shouldCreateStandardLineExpiringPrevious(){
        CreateStandardLineDocumentDto request = new CreateStandardLineDocumentDto(DISPLAY_NAME,"url",uuid,LocalDate.now().plusDays(1));

        when(documentClient.createDocument(SL_EXT_REF, request.getDisplayName(), ManagedDocumentType.STANDARD_LINE)).thenReturn(NEW_DOCUMENT_UUID);
        when(standardLineRepository.findStandardLinesByTopicAndExpires(uuid , END_OF_DAY)).thenReturn(new StandardLine(DOCUMENT_UUID, DISPLAY_NAME, uuid, END_OF_DAY));

        standardLineService.createStandardLineDocument(request);

        verify(documentClient,times(1)).createDocument(SL_EXT_REF, DISPLAY_NAME,ManagedDocumentType.STANDARD_LINE );
        verify(standardLineRepository, times(1)).findStandardLinesByTopicAndExpires(uuid, END_OF_DAY);
        verify(standardLineRepository, times(2)).save(any());
        verify(documentClient).deleteDocument(SL_EXT_REF, DOCUMENT_UUID);
        verify(documentClient).processDocument(ManagedDocumentType.STANDARD_LINE, NEW_DOCUMENT_UUID, "url");
        verifyNoMoreInteractions(standardLineRepository);
        verifyNoMoreInteractions(documentClient);
    }

    @Test(expected = EntityNotFoundException.class)
    public void shouldNotGetCaseWithValidParamsNotFoundException() {

        standardLineService.getStandardLines(uuid);
    }
}
