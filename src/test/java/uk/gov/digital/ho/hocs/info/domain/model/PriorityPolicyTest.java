package uk.gov.digital.ho.hocs.info.domain.model;

import org.junit.Before;
import org.junit.Test;
import uk.gov.digital.ho.hocs.info.api.dto.PriorityPolicyDto;

import static org.assertj.core.api.Assertions.assertThat;

public class PriorityPolicyTest {

    private Long id = 55L;

    private String policyType = "testPolicyType";

    private String caseType = "TEST_CASE_TYPE";

    private String config = "{\"PropertyA\": \"Valuea\", \"PropertyB\": \"ValueB\"}";

    private PriorityPolicy policy;

    @Before
    public void before() {
        policy = new PriorityPolicy(id, policyType, caseType, config);
    }

    @Test
    public void toDto() {
        PriorityPolicyDto dto = policy.toDto();

        assertThat(dto).isNotNull();
        assertThat(dto.getPolicyType()).isEqualTo(policyType);
        assertThat(dto.getCaseType()).isEqualTo(caseType);
        assertThat(dto.getConfig()).isEqualTo(config);
    }

    @Test
    public void getId() {
        assertThat(policy.getId()).isEqualTo(id);
    }

    @Test
    public void getCaseType() {
        assertThat(policy.getCaseType()).isEqualTo(caseType);
    }

    @Test
    public void getPolicyType() {
        assertThat(policy.getPolicyType()).isEqualTo(policyType);
    }

    @Test
    public void getConfig() {
        assertThat(policy.getConfig()).isEqualTo(config);
    }

}
