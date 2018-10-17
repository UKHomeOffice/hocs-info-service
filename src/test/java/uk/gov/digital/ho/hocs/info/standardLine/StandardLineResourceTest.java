package uk.gov.digital.ho.hocs.info.standardLine;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uk.gov.digital.ho.hocs.info.dto.GetStandardLineResponse;
import uk.gov.digital.ho.hocs.info.entities.StandardLine;
import uk.gov.digital.ho.hocs.info.exception.EntityPermissionException;

import java.util.ArrayList;
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
    public void shouldReturnStandardLineForPrimaryTopic() throws EntityPermissionException {

        when(standardLineService.getStandardLines(uuid)).thenReturn(new ArrayList<>());

        ResponseEntity<GetStandardLineResponse> response =
                standardLineResource.getStandardLinesForPrimaryTopic(uuid);

        verify(standardLineService, times(1)).getStandardLines(uuid);
        verifyNoMoreInteractions(standardLineService);
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}