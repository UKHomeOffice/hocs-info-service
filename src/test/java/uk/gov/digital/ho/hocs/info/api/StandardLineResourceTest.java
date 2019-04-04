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
import uk.gov.digital.ho.hocs.info.domain.model.StandardLine;

import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class StandardLineResourceTest {

    @Mock
    private StandardLineService standardLineService;

    private StandardLineResource standardLineResource;

    UUID uuid = UUID.randomUUID();

    @Before
    public void setUp() {
        standardLineResource = new StandardLineResource(standardLineService);
    }

    @Test
    public void shouldReturnStandardLines() {

        when(standardLineService.getActiveStandardLines()).thenReturn(Set.of(new StandardLine()));

        ResponseEntity<Set<GetStandardLineResponse>> response = standardLineResource.getStandardLines();

        verify(standardLineService, times(1)).getActiveStandardLines();
        verifyNoMoreInteractions(standardLineService);
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void shouldReturnStandardLineForPrimaryTopic() {

        when(standardLineService.getStandardLineForTopic(uuid)).thenReturn(new StandardLine());

        ResponseEntity<GetStandardLineResponse> response =
                standardLineResource.getStandardLinesForPrimaryTopic(uuid);

        verify(standardLineService, times(1)).getStandardLineForTopic(uuid);
        verifyNoMoreInteractions(standardLineService);
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void shouldCreateStandardLineForTopic() {
        CreateStandardLineDocumentDto standardLineDocumentDto = new CreateStandardLineDocumentDto();

        ResponseEntity response =
                standardLineResource.createDocument(standardLineDocumentDto);

        verify(standardLineService, times(1)).createStandardLine(standardLineDocumentDto.getDisplayName(), standardLineDocumentDto.getTopicUUID(), standardLineDocumentDto.getExpires(), standardLineDocumentDto.getS3UntrustedUrl());
        verifyNoMoreInteractions(standardLineService);
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }


}