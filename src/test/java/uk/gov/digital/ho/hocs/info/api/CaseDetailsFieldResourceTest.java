package uk.gov.digital.ho.hocs.info.api;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;
import uk.gov.digital.ho.hocs.info.api.dto.CaseDetailsFieldDto;

import java.util.List;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CaseDetailsFieldResourceTest {

    @Mock
    private CaseDetailsFieldService caseDetailsFieldService;

    private CaseDetailsFieldResource caseDetailsFieldResource;

    private final String caseType = "Type1";


    @Before
    public void setUp() {
        caseDetailsFieldResource = new CaseDetailsFieldResource(caseDetailsFieldService);
    }

    @Test
    public void getCaseDetailsFieldsByCaseType() {

        String nameA = "nameA";
        String componentA = "componentA";
        String propertyA = "propertyA";
        String nameB = "nameB";
        String componentB = "componentB";
        String propertyB = "propertyB";

        CaseDetailsFieldDto fieldA = new CaseDetailsFieldDto(nameA, componentA, propertyA);
        CaseDetailsFieldDto fieldB = new CaseDetailsFieldDto(nameB, componentB, propertyB);

        when(caseDetailsFieldService.getCaseDetailsFieldsByCaseType(caseType)).thenReturn(List.of(fieldA, fieldB));

        ResponseEntity<List<CaseDetailsFieldDto>> result = caseDetailsFieldResource.getCaseDetailsFieldsByCaseType(caseType);

        Assert.assertEquals("Status code incorrect", 200, result.getStatusCode().value());
        Assert.assertNotNull("Body should be defined", result.getBody());
        Assert.assertEquals("There should be 2 fields returned", 2, result.getBody().size());
        Assert.assertEquals("Field A name does not match", nameA, result.getBody().get(0).getName());
        Assert.assertEquals("Field B name does not match", nameB, result.getBody().get(1).getName());
        Assert.assertEquals("Field A component does not match", componentA, result.getBody().get(0).getComponent());
        Assert.assertEquals("Field B component does not match", componentB, result.getBody().get(1).getComponent());
        Assert.assertEquals("Field A props does not match", propertyA, result.getBody().get(0).getProps());
        Assert.assertEquals("Field B props does not match", propertyB, result.getBody().get(1).getProps());

        verify(caseDetailsFieldService).getCaseDetailsFieldsByCaseType(caseType);
        verifyNoMoreInteractions(caseDetailsFieldService);
    }

    @Test
    public void getCaseDetailsFieldsByCaseType_EmptyList() {

        when(caseDetailsFieldService.getCaseDetailsFieldsByCaseType(caseType)).thenReturn(List.of());

        ResponseEntity<List<CaseDetailsFieldDto>> result = caseDetailsFieldResource.getCaseDetailsFieldsByCaseType(caseType);

        Assert.assertEquals("Status code incorrect", 200, result.getStatusCode().value());
        Assert.assertNotNull("Body should be defined", result.getBody());
        Assert.assertEquals("There should be 0 fields returned", 0, result.getBody().size());

        verify(caseDetailsFieldService).getCaseDetailsFieldsByCaseType(caseType);
        verifyNoMoreInteractions(caseDetailsFieldService);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getCaseDetailsFieldsByCaseType_throwsException() {

        when(caseDetailsFieldService.getCaseDetailsFieldsByCaseType(caseType)).thenThrow(new IllegalArgumentException("Test Exception"));

        caseDetailsFieldResource.getCaseDetailsFieldsByCaseType(caseType);

    }

}