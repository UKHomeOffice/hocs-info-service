package uk.gov.digital.ho.hocs.info.api;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;
import uk.gov.digital.ho.hocs.info.api.dto.PriorityPolicyDto;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PriorityPolicyResourceTest {

    @Mock
    private PriorityPolicyService priorityPolicyService;

    private PriorityPolicyResource priorityPolicyResource;

    @Before
    public void setUp() {
        priorityPolicyResource = new PriorityPolicyResource(priorityPolicyService);
    }

    @Test
    public void getByCaseType() {
        String caseType = "testCaseType";

        List<PriorityPolicyDto> priorityPolicyDtos = Collections.singletonList(new PriorityPolicyDto("policyType", caseType, "config"));

        when(priorityPolicyService.getByCaseType(caseType)).thenReturn(priorityPolicyDtos);

        ResponseEntity<List<PriorityPolicyDto>> result = priorityPolicyResource.getByCaseType(caseType);


        Assert.assertEquals("Status code incorrect", 200, result.getStatusCode().value());
        Assert.assertEquals("Returned list do not match", priorityPolicyDtos, result.getBody());

        verify(priorityPolicyService).getByCaseType(caseType);
        verifyNoMoreInteractions(priorityPolicyService);
    }

}