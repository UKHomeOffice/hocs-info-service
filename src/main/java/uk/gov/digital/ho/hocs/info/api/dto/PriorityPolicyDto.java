package uk.gov.digital.ho.hocs.info.api.dto;

import com.fasterxml.jackson.annotation.JsonRawValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor()
@Getter
public class PriorityPolicyDto {

    private String policyType;

    private String caseType;

    @JsonRawValue
    private String config;

}
