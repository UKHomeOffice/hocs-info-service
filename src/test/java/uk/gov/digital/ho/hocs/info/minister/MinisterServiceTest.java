package uk.gov.digital.ho.hocs.info.minister;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.digital.ho.hocs.info.Minister.MinisterService;
import uk.gov.digital.ho.hocs.info.entities.Minister;
import uk.gov.digital.ho.hocs.info.repositories.MinisterRepository;

import java.util.HashSet;
import java.util.Set;

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