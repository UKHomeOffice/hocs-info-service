package uk.gov.digital.ho.hocs.info.api;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.digital.ho.hocs.info.api.dto.CreateCorrespondentTypeDto;
import uk.gov.digital.ho.hocs.info.domain.model.CorrespondentType;

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

    @Test
    public void shouldCreateNewCorrespondentType() {
        CreateCorrespondentTypeDto request = new CreateCorrespondentTypeDto("name","NAME");

        when(correspondentTypeService.createCorrespondentType(any(), any())).thenReturn(new CorrespondentType());

        correspondentTypeResource.createCorrespondentType(request);

        verify(correspondentTypeService, times(1)).createCorrespondentType(any(), any());
        verifyNoMoreInteractions(correspondentTypeService);
    }
}