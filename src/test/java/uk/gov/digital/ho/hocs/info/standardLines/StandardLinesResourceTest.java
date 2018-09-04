package uk.gov.digital.ho.hocs.info.standardLines;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uk.gov.digital.ho.hocs.info.dto.GetStandardLinesResponse;
import uk.gov.digital.ho.hocs.info.dto.GetTemplateResponse;
import uk.gov.digital.ho.hocs.info.entities.StandardLines;
import uk.gov.digital.ho.hocs.info.entities.Template;
import uk.gov.digital.ho.hocs.info.exception.EntityPermissionException;
import uk.gov.digital.ho.hocs.info.template.TemplateResource;
import uk.gov.digital.ho.hocs.info.template.TemplateService;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class StandardLinesResourceTest {

    public static final String MIN = "MIN";
    @Mock
    private StandardLinesService standardLinesService;

    private StandardLinesResource standardLinesResource;

    UUID uuid = UUID.randomUUID();

    @Before
    public void setUp() {
        standardLinesResource = new StandardLinesResource(standardLinesService);
    }

    @Test
    public void shouldReturnTemplateForRequestedCaseType() throws EntityPermissionException {

        when(standardLinesService.getStandardLines(MIN,uuid)).thenReturn(new StandardLines());

        ResponseEntity<GetStandardLinesResponse> response =
                standardLinesResource.getTemplateForCaseTypes(MIN, uuid);

        verify(standardLinesService, times(1)).getStandardLines(MIN, uuid);
        verifyNoMoreInteractions(standardLinesService);
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}