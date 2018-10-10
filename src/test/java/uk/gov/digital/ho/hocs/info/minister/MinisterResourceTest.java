package uk.gov.digital.ho.hocs.info.minister;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;
import uk.gov.digital.ho.hocs.info.Minister.MinisterResource;
import uk.gov.digital.ho.hocs.info.Minister.MinisterService;
import uk.gov.digital.ho.hocs.info.dto.GetMinistersResponse;

import java.util.HashSet;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MinisterResourceTest {

    @Mock
    private MinisterService ministerService;

    private MinisterResource ministerResource;


    @Before
    public void setUp() {
        ministerResource = new MinisterResource(ministerService);
    }

    @Test
    public void shouldReturnAllMinisters() {

        when(ministerService.getMinisters()).thenReturn(new HashSet<>());

        ResponseEntity<GetMinistersResponse> response = ministerResource.getAllMinisters();
        verify(ministerService, times(1)).getMinisters();
        verifyNoMoreInteractions(ministerService);
    }
}