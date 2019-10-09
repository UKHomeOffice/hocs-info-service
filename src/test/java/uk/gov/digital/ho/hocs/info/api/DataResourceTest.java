package uk.gov.digital.ho.hocs.info.api;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@RunWith(MockitoJUnitRunner.class)
public class DataResourceTest {

    @Mock
    private DataService dataService;

    private DataResource dataResource;

    @Before
    public void setUp() {
        dataResource = new DataResource(dataService);
    }

    @Test
    public void shouldGetCohorts() {
        when(dataService.getCohorts()).thenReturn(Arrays.asList(new String[]{"a","b","c"}));

        ResponseEntity result = dataResource.getCohorts();
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isEqualTo(Arrays.asList(new String[]{"a","b","c"}));
        verify(dataService, times(1)).getCohorts();
        verifyNoMoreInteractions(dataService);
    }
}
