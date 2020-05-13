package uk.gov.digital.ho.hocs.info.api;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.digital.ho.hocs.info.api.dto.PriorityPolicyDto;
import uk.gov.digital.ho.hocs.info.domain.model.PriorityPolicy;
import uk.gov.digital.ho.hocs.info.domain.repository.PriorityPolicyRepository;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class PriorityPolicyServiceTest {

    @Mock
    private PriorityPolicyRepository priorityPolicyRepository;

    private PriorityPolicyService priorityPolicyService;

    @Before
    public void setUp() {
        this.priorityPolicyService = new PriorityPolicyService(priorityPolicyRepository);
    }

    @Test
    public void getByCaseType() {
        String caseType = "testCaseType";

        List<PriorityPolicy> priorityPolicies = Collections.singletonList(new PriorityPolicy(25L, "policyType", caseType, "config"));

        when(priorityPolicyRepository.findAllByCaseType(caseType)).thenReturn(priorityPolicies);

        List<PriorityPolicyDto> results = priorityPolicyService.getByCaseType(caseType);


        Assert.assertNotNull(results);
        Assert.assertEquals("Returned list size do not match", priorityPolicies.size(), results.size());
        Assert.assertEquals("Returned item property do not match", priorityPolicies.get(0).getCaseType(), results.get(0).getCaseType());
        Assert.assertEquals("Returned item property do not match", priorityPolicies.get(0).getConfig(), results.get(0).getConfig());
        Assert.assertEquals("Returned item property do not match", priorityPolicies.get(0).getPolicyType(), results.get(0).getPolicyType());

        verify(priorityPolicyRepository).findAllByCaseType(caseType);
        verifyNoMoreInteractions(priorityPolicyRepository);
    }
}