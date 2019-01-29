package uk.gov.digital.ho.hocs.info.api;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.digital.ho.hocs.info.client.caseworkclient.CaseworkClient;
import uk.gov.digital.ho.hocs.info.client.caseworkclient.dto.GetCaseworkCaseDataResponse;
import uk.gov.digital.ho.hocs.info.client.caseworkclient.dto.GetTopicResponse;
import uk.gov.digital.ho.hocs.info.client.documentClient.DocumentClient;
import uk.gov.digital.ho.hocs.info.client.documentClient.model.ManagedDocumentType;
import uk.gov.digital.ho.hocs.info.api.dto.CreateStandardLineDocumentDto;
import uk.gov.digital.ho.hocs.info.domain.exception.ApplicationExceptions;
import uk.gov.digital.ho.hocs.info.domain.model.StandardLine;
import uk.gov.digital.ho.hocs.info.domain.repository.StandardLineRepository;

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

    private StandardLineService standardLineService;

    private static final UUID SL_EXT_REF = UUID.fromString("77777777-7777-7777-7777-777777777777");
    private static final UUID uuid = UUID.randomUUID();
    private static final UUID DOCUMENT_UUID = UUID.randomUUID();
    private static final UUID NEW_DOCUMENT_UUID = UUID.randomUUID();
    private static final LocalDateTime END_OF_DAY = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
    private static final String DISPLAY_NAME = "dn";

    @Before
    public void setUp() {
        this.standardLineService = new StandardLineService(standardLineRepository, documentClient);
    }

    @Test
    public void shouldReturnStandardLineForPrimaryTopic(){
        when(standardLineRepository.findStandardLinesByTopicAndExpires(uuid, END_OF_DAY)).thenReturn(new StandardLine());
        standardLineService.getStandardLineForTopic(uuid);
        verify(standardLineRepository, times(1)).findStandardLinesByTopicAndExpires(uuid, END_OF_DAY);
        verifyNoMoreInteractions(standardLineRepository);
    }

    @Test
    public void shouldCreateNewStandardLine(){
        CreateStandardLineDocumentDto request = new CreateStandardLineDocumentDto(DISPLAY_NAME,"url",uuid,LocalDate.now().plusDays(1));

        when(documentClient.createDocument(any(UUID.class), eq(request.getDisplayName()), eq(ManagedDocumentType.STANDARD_LINE))).thenReturn(DOCUMENT_UUID);
        when(standardLineRepository.findStandardLinesByTopicAndExpires(uuid, END_OF_DAY)).thenReturn(null);

        standardLineService.createStandardLine(request.getDisplayName(), request.getTopicUUID(), request.getExpires(), request.getS3UntrustedUrl());

        verify(documentClient,times(1)).createDocument(any(UUID.class), eq(DISPLAY_NAME), eq(ManagedDocumentType.STANDARD_LINE));
        verify(standardLineRepository, times(1)).findStandardLinesByTopicAndExpires(uuid, END_OF_DAY);
        verify(standardLineRepository, times(1)).save(any());
        verify(documentClient).processDocument(ManagedDocumentType.STANDARD_LINE, DOCUMENT_UUID, "url");
        verifyNoMoreInteractions(standardLineRepository);
        verifyNoMoreInteractions(documentClient);
    }

    @Test
    public void shouldCreateStandardLineExpiringPrevious(){
        CreateStandardLineDocumentDto request = new CreateStandardLineDocumentDto(DISPLAY_NAME,"url",uuid,LocalDate.now().plusDays(1));

        when(documentClient.createDocument(any(UUID.class), eq(request.getDisplayName()), eq(ManagedDocumentType.STANDARD_LINE))).thenReturn(NEW_DOCUMENT_UUID);
        when(standardLineRepository.findStandardLinesByTopicAndExpires(uuid , END_OF_DAY)).thenReturn(new StandardLine(DISPLAY_NAME, uuid, END_OF_DAY));

        standardLineService.createStandardLine(request.getDisplayName(), request.getTopicUUID(), request.getExpires(), request.getS3UntrustedUrl());

        verify(documentClient,times(1)).createDocument(any(UUID.class), eq(DISPLAY_NAME), eq(ManagedDocumentType.STANDARD_LINE));
        verify(standardLineRepository, times(1)).findStandardLinesByTopicAndExpires(uuid, END_OF_DAY);
        verify(standardLineRepository, times(2)).save(any());
        verify(documentClient).deleteDocument(any(UUID.class));
        verify(documentClient).processDocument(ManagedDocumentType.STANDARD_LINE, NEW_DOCUMENT_UUID, "url");
        verifyNoMoreInteractions(standardLineRepository);
        verifyNoMoreInteractions(documentClient);
    }
}
