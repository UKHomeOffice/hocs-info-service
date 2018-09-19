package uk.gov.digital.ho.hocs.info.standardLine;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uk.gov.digital.ho.hocs.info.dto.GetStandardLineKeyResponse;
import uk.gov.digital.ho.hocs.info.dto.GetStandardLineResponse;
import uk.gov.digital.ho.hocs.info.entities.StandardLine;
import uk.gov.digital.ho.hocs.info.exception.EntityPermissionException;

import java.util.ArrayList;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class StandardLineResourceTest {

    public static final String MIN = "MIN";
    @Mock
    private StandardLineService standardLineService;

    private StandardLineResource standardLineResource;

    UUID uuid = UUID.randomUUID();

    @Before
    public void setUp() {
        standardLineResource = new StandardLineResource(standardLineService);
    }

    @Test
    public void shouldReturnTemplateForRequestedCaseType() throws EntityPermissionException {

        when(standardLineService.getStandardLines(MIN,uuid)).thenReturn(new ArrayList<StandardLine>(){{new StandardLine();}});

        ResponseEntity<GetStandardLineResponse> response =
                standardLineResource.getTemplateForCaseTypes(MIN, uuid);

        verify(standardLineService, times(1)).getStandardLines(MIN, uuid);
        verifyNoMoreInteractions(standardLineService);
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void shouldReturnTemplateKey() throws EntityPermissionException {

        when(standardLineService.getStandardLineKey(uuid)).thenReturn(new StandardLine());

        ResponseEntity<GetStandardLineKeyResponse> response =
                standardLineResource.getStandardLineKey(MIN, uuid);

        verify(standardLineService, times(1)).getStandardLineKey(uuid);
        verifyNoMoreInteractions(standardLineService);
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}