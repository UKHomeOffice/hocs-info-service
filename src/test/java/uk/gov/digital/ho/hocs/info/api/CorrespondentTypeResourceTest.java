package uk.gov.digital.ho.hocs.info.api;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.digital.ho.hocs.info.api.CorrespondentTypeResource;
import uk.gov.digital.ho.hocs.info.api.CorrespondentTypeService;

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

        when(correspondentTypeService.getAllCorrespondentTypes()).thenReturn(new HashSet<>());

        correspondentTypeResource.getCorrespondentTypes();

        verify(correspondentTypeService, times(1)).getAllCorrespondentTypes();
        verifyNoMoreInteractions(correspondentTypeService);
    }

}