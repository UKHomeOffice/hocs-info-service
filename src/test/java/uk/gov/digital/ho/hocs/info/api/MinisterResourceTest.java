package uk.gov.digital.ho.hocs.info.api;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.digital.ho.hocs.info.api.MinisterResource;
import uk.gov.digital.ho.hocs.info.api.MinisterService;

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

        ministerResource.getAllMinisters();
        verify(ministerService, times(1)).getMinisters();
        verifyNoMoreInteractions(ministerService);
    }
}