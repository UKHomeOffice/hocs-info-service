package uk.gov.digital.ho.hocs.info.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.digital.ho.hocs.info.api.dto.PriorityPolicyDto;
import uk.gov.digital.ho.hocs.info.domain.model.PriorityPolicy;
import uk.gov.digital.ho.hocs.info.domain.repository.PriorityPolicyRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Deprecated(forRemoval = true)
public class PriorityPolicyService {

    private final PriorityPolicyRepository priorityPolicyRepository;

    @Autowired
    public PriorityPolicyService(PriorityPolicyRepository priorityPolicyRepository) {
        this.priorityPolicyRepository = priorityPolicyRepository;
    }

    @Deprecated(forRemoval = true)
    public List<PriorityPolicyDto> getByCaseType(String caseType) {
        log.debug("Getting Priority Policies for {}", caseType);
        List<PriorityPolicy> policies = priorityPolicyRepository.findAllByCaseType(caseType);
        log.info("Got {} Priority Policies", policies.size());
        return policies.stream().map(PriorityPolicy::toDto).collect(Collectors.toList());
    }

}
