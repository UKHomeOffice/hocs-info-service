package uk.gov.digital.ho.hocs.info.standardLine;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uk.gov.digital.ho.hocs.info.dto.CreateStandardLineDocumentDto;
import uk.gov.digital.ho.hocs.info.dto.GetStandardLineResponse;
import uk.gov.digital.ho.hocs.info.entities.StandardLine;

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
    public void shouldReturnStandardLineForPrimaryTopic() {

        when(standardLineService.getStandardLines(uuid)).thenReturn(new StandardLine());

        ResponseEntity<GetStandardLineResponse> response =
                standardLineResource.getStandardLinesForPrimaryTopic(uuid);

        verify(standardLineService, times(1)).getStandardLines(uuid);
        verifyNoMoreInteractions(standardLineService);
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void shouldReturnNotFoundWhenNoStandardLineForPrimaryTopic() {

        when(standardLineService.getStandardLines(uuid)).thenReturn(null);

        ResponseEntity<GetStandardLineResponse> response =
                standardLineResource.getStandardLinesForPrimaryTopic(uuid);

        verify(standardLineService, times(1)).getStandardLines(uuid);
        verifyNoMoreInteractions(standardLineService);
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void shouldCreateStandardLineForTopic() {
        CreateStandardLineDocumentDto createStandardLineDocumentDto = new CreateStandardLineDocumentDto();

        ResponseEntity response =
                standardLineResource.createDocument(createStandardLineDocumentDto);

        verify(standardLineService, times(1)).createStandardLineDocument(createStandardLineDocumentDto);
        verifyNoMoreInteractions(standardLineService);
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }


}