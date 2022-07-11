package uk.gov.digital.ho.hocs.info.domain.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uk.gov.digital.ho.hocs.info.domain.model.PriorityPolicy;

import java.util.List;

@Deprecated(forRemoval = true)
@Repository
public interface PriorityPolicyRepository extends CrudRepository<PriorityPolicy, Long> {

    @Deprecated(forRemoval = true)
    List<PriorityPolicy> findAllByCaseType(String caseType);

}
