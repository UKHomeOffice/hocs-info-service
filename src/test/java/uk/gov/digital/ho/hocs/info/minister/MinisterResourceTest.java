package uk.gov.digital.ho.hocs.info.minister;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uk.gov.digital.ho.hocs.info.dto.GetMinisterResponse;
import uk.gov.digital.ho.hocs.info.entities.Minister;
import uk.gov.digital.ho.hocs.info.topic.MinisterService;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MinisterResourceTest {

    @Mock
    private MinisterService ministerService;
    private MinisterResource ministerResource;

    private final String[] ROLE = {"XXX"};

    @Before
    public void setUp() {
        ministerResource = new MinisterResource(ministerService);
    }


    @Test
    public void shouldReturnMinisterForTopic() {
        Minister theMinister = new Minister(99, "HM Secretary of State for the Home Department");
        when(ministerService.getMinisterFromTopicId(99L)).thenReturn(theMinister);

        ResponseEntity<GetMinisterResponse> response = ministerResource.getMinisterFromTopic(ROLE, 99L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getMinister()).isEqualTo(theMinister);
    }
}
