package uk.gov.digital.ho.hocs.info.template;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uk.gov.digital.ho.hocs.info.dto.GetTemplateResponse;
import uk.gov.digital.ho.hocs.info.exception.EntityPermissionException;

import java.util.ArrayList;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TemplateResourceTest {

    public static final String MIN = "MIN";
    @Mock
    private TemplateService templateService;

    private TemplateResource templateResource;

    private final UUID TEMPLATE_UUID = UUID.randomUUID();

    @Before
    public void setUp() {
        templateResource = new TemplateResource(templateService);
    }

    @Test
    public void shouldReturnTemplateForRequestedCaseType() throws EntityPermissionException {

        when(templateService.getTemplates(MIN)).thenReturn(new ArrayList<>());

        ResponseEntity<GetTemplateResponse> response =
                templateResource.getTemplatesForCaseType(MIN);

        verify(templateService, times(1)).getTemplates(MIN);
        verifyNoMoreInteractions(templateService);
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

//    @Test(expected = EntityNotFoundException.class)
//    public void shouldThrowEntityNotFoundExceptionWhenMemberNotFound(){
//
//    }
}