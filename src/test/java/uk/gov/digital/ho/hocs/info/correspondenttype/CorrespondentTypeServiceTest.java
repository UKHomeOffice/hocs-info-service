package uk.gov.digital.ho.hocs.info.correspondenttype;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.digital.ho.hocs.info.api.correspondentType.CorrespondentTypeService;
import uk.gov.digital.ho.hocs.info.domain.repository.CorrespondentTypeRepository;

import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class CorrespondentTypeServiceTest {

    @Mock
    private CorrespondentTypeRepository correspondentTypeRepository;

    private CorrespondentTypeService correspondentTypeService;


    @Before
    public void setUp() {
        this.correspondentTypeService = new CorrespondentTypeService(correspondentTypeRepository);
    }

    @Test
    public void shouldreturnAllCorrespondentTypes() {
        when(correspondentTypeRepository.findAll()).thenReturn(new HashSet<>());

        correspondentTypeService.getCorrespondentTypes();
        verify(correspondentTypeRepository, times(1)).findAll();
        verifyNoMoreInteractions(correspondentTypeRepository);


    }
}