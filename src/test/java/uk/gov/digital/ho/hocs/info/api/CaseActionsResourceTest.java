package uk.gov.digital.ho.hocs.info.api;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uk.gov.digital.ho.hocs.info.api.dto.CaseActionTypeDto;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CaseActionsResourceTest {

    @Mock
    private CaseActionsTypeService caseActionsTypeService;

    private CaseActionsResource caseActionsResource;

    @Before
    public void setUp() {
        caseActionsResource = new CaseActionsResource(caseActionsTypeService);
    }

    @Test
    public void getCaseActionsByType() {

        String caseType = "CASE_TYPE";

        when(caseActionsTypeService.getCaseActionsByCaseType(caseType)).thenReturn(List.of());

        ResponseEntity<List<CaseActionTypeDto>> output = caseActionsResource.getCaseActionsByType(caseType);

        assertEquals(output.getStatusCode(), HttpStatus.OK);
        assertNotNull(output.getBody());
    }

}