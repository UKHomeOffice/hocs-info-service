package uk.gov.digital.ho.hocs.info.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import uk.gov.digital.ho.hocs.info.api.dto.PriorityPolicyDto;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@javax.persistence.Entity
@Getter
@Table(name = "priority_policy")
public class PriorityPolicy {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "policy_type", insertable = false, updatable = false)
    private String policyType;

    @Column(name = "applicable_case_type", insertable = false, updatable = false)
    private String caseType;

    @Column(name = "config", insertable = false, updatable = false)
    private String config;


    public PriorityPolicyDto toDto(){
        return new PriorityPolicyDto(policyType, caseType, config);
    }

}
