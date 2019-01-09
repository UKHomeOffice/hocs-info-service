package uk.gov.digital.ho.hocs.info.api;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.digital.ho.hocs.info.api.MinisterService;
import uk.gov.digital.ho.hocs.info.domain.repository.MinisterRepository;

import java.util.HashSet;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MinisterServiceTest {

    @Mock
    private MinisterRepository ministerRepository;

    private MinisterService ministerService;


    @Before
    public void setUp() {
        this.ministerService = new MinisterService(ministerRepository);
    }

    @Test
    public void shouldReturnAllMinisters() {

        when(ministerRepository.findAll()).thenReturn(new HashSet<>());

        ministerService.getMinisters();
        verify(ministerRepository, times(1)).findAll();
        verifyNoMoreInteractions(ministerRepository);
    }
}