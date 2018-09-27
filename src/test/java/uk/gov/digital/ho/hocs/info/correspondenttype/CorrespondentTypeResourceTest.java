package uk.gov.digital.ho.hocs.info.correspondenttype;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uk.gov.digital.ho.hocs.info.casetype.CaseTypeResource;
import uk.gov.digital.ho.hocs.info.casetype.CaseTypeService;
import uk.gov.digital.ho.hocs.info.correspondentType.CorrespondentTypeResource;
import uk.gov.digital.ho.hocs.info.correspondentType.CorrespondentTypeService;
import uk.gov.digital.ho.hocs.info.dto.CaseTypeDto;
import uk.gov.digital.ho.hocs.info.dto.GetCaseTypesResponse;
import uk.gov.digital.ho.hocs.info.dto.GetCorrespondentTypeResponse;
import uk.gov.digital.ho.hocs.info.entities.CaseTypeEntity;
import uk.gov.digital.ho.hocs.info.entities.CorrespondentType;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
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

        ResponseEntity<GetCorrespondentTypeResponse> response =
                correspondentTypeResource.getCorrespondentTypes();

        verify(correspondentTypeService, times(1)).getCorrespondentTypes();
        verifyNoMoreInteractions(correspondentTypeService);
    }

}