package uk.gov.digital.ho.hocs.info.api;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uk.gov.digital.ho.hocs.info.api.StandardLineResource;
import uk.gov.digital.ho.hocs.info.api.StandardLineService;
import uk.gov.digital.ho.hocs.info.api.dto.CreateStandardLineDocumentDto;
import uk.gov.digital.ho.hocs.info.api.dto.GetStandardLineResponse;
import uk.gov.digital.ho.hocs.info.api.dto.UpdateStandardLineDto;
import uk.gov.digital.ho.hocs.info.domain.model.StandardLine;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class StandardLineResourceTest {

    @Mock
    private StandardLineService standardLineService;

    private StandardLineResource standardLineResource;

    private UUID uuid = UUID.randomUUID();
    private UUID standardLineUUID = UUID.randomUUID();

    @Before
    public void setUp() {
        standardLineResource = new StandardLineResource(standardLineService);
    }

    @Test
    public void shouldReturnStandardLines() {

        when(standardLineService.getActiveStandardLines()).thenReturn(Set.of(new StandardLine()));

        ResponseEntity<Set<GetStandardLineResponse>> response = standardLineResource.getStandardLines();

        verify(standardLineService).getActiveStandardLines();
        verifyNoMoreInteractions(standardLineService);
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void shouldReturnStandardLineForPrimaryTopic() {

        when(standardLineService.getStandardLineForTopic(uuid)).thenReturn(new StandardLine());

        ResponseEntity<GetStandardLineResponse> response =
                standardLineResource.getStandardLinesForPrimaryTopic(uuid);

        verify(standardLineService).getStandardLineForTopic(uuid);
        verifyNoMoreInteractions(standardLineService);
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void shouldCreateStandardLineForTopic() {
        CreateStandardLineDocumentDto standardLineDocumentDto = new CreateStandardLineDocumentDto();

        ResponseEntity response =
                standardLineResource.createStandardLine(standardLineDocumentDto);

        verify(standardLineService).createStandardLine(standardLineDocumentDto.getDisplayName(), standardLineDocumentDto.getTopicUUID(), standardLineDocumentDto.getExpires(), standardLineDocumentDto.getS3UntrustedUrl());
        verifyNoMoreInteractions(standardLineService);
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void getStandardLine(){
        when(standardLineService.getStandardLine(standardLineUUID)).thenReturn(new StandardLine("DisplayName", uuid, LocalDateTime.now()));

        ResponseEntity<GetStandardLineResponse> response = standardLineResource.getStandardLine(standardLineUUID);
        verify(standardLineService).getStandardLine(standardLineUUID);
        verifyNoMoreInteractions(standardLineService);
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getDisplayName()).isEqualTo("DisplayName");
        assertThat(response.getBody().getTopicUUID()).isEqualTo(uuid);
    }

    @Test
    public void getStandardLine_lineNotFound(){

        ResponseEntity<GetStandardLineResponse> response = standardLineResource.getStandardLine(standardLineUUID);
        verify(standardLineService).getStandardLine(standardLineUUID);
        verifyNoMoreInteractions(standardLineService);
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNull();
    }

    @Test
    public void getAllStandardLines(){
        List<StandardLine> standardLines = List.of(new StandardLine("DisplayName", uuid, LocalDateTime.now()));

        when(standardLineService.getAllStandardLines()).thenReturn(standardLines);

        ResponseEntity<List<GetStandardLineResponse>> response = standardLineResource.getAllStandardLines();
        verify(standardLineService).getAllStandardLines();
        verifyNoMoreInteractions(standardLineService);
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().size()).isEqualTo(1);
        assertThat(response.getBody().get(0).getDisplayName()).isEqualTo("DisplayName");
        assertThat(response.getBody().get(0).getTopicUUID()).isEqualTo(uuid);
    }

    @Test
    public void updateStandardLine(){
        UpdateStandardLineDto updateStandardLineDto = new UpdateStandardLineDto("NewDisplayName", LocalDate.now().plusDays(10));
        ResponseEntity response = standardLineResource.updateStandardLine(standardLineUUID, updateStandardLineDto);

        verify(standardLineService).updateStandardLine(standardLineUUID, updateStandardLineDto);
        verifyNoMoreInteractions(standardLineService);
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void expireStandardLine(){
        ResponseEntity response = standardLineResource.expireStandardLine(standardLineUUID);

        verify(standardLineService).expireStandardLine(standardLineUUID);
        verifyNoMoreInteractions(standardLineService);
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void deleteStandardLine(){
        ResponseEntity response = standardLineResource.deleteStandardLine(standardLineUUID);

        verify(standardLineService).deleteStandardLine(standardLineUUID);
        verifyNoMoreInteractions(standardLineService);
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void getStandardLinesForUser(){
        List<StandardLine> standardLines = List.of(new StandardLine("DisplayName", uuid, LocalDateTime.now()));

        when(standardLineService.getStandardLinesForUser(any())).thenReturn(standardLines);

        ResponseEntity<List<GetStandardLineResponse>> response = standardLineResource.getStandardLinesForUser(any());
        verify(standardLineService).getStandardLinesForUser(any());
        verifyNoMoreInteractions(standardLineService);
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().size()).isEqualTo(1);
        assertThat(response.getBody().get(0).getDisplayName()).isEqualTo("DisplayName");
        assertThat(response.getBody().get(0).getTopicUUID()).isEqualTo(uuid);
    }
}
