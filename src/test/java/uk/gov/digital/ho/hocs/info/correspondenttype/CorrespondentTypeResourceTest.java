package uk.gov.digital.ho.hocs.info.correspondenttype;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.digital.ho.hocs.info.api.correspondentType.CorrespondentTypeResource;
import uk.gov.digital.ho.hocs.info.api.correspondentType.CorrespondentTypeService;

import java.util.HashSet;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CorrespondentTypeResourceTest {

    @Mock
    private CorrespondentTypeService correspondentTypeService;

    private CorrespondentTypeResource correspondentTypeResource;


    @Before
    public void setUp() {
        correspondentTypeResource = new CorrespondentTypeResource(correspondentTypeService);
    }

    @Test
    public void shouldReturnAllCorrespondentTypes() {

        when(correspondentTypeService.getCorrespondentTypes()).thenReturn(new HashSet<>());

        correspondentTypeResource.getCorrespondentTypes();

        verify(correspondentTypeService, times(1)).getCorrespondentTypes();
        verifyNoMoreInteractions(correspondentTypeService);
    }

}