package uk.gov.digital.ho.hocs.info.domain.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uk.gov.digital.ho.hocs.info.domain.model.PriorityPolicy;

import java.util.List;

@Repository
public interface PriorityPolicyRepository extends CrudRepository<PriorityPolicy, Long> {

    List<PriorityPolicy> findAllByCaseType(String caseType);
}