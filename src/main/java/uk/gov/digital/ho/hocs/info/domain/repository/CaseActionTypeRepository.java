package uk.gov.digital.ho.hocs.info.domain.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uk.gov.digital.ho.hocs.info.domain.model.CaseTypeAction;

import java.util.List;

@Repository
public interface CaseActionTypeRepository extends CrudRepository<CaseTypeAction, Long> {

    List<CaseTypeAction> findAllByCaseTypeAndActiveIsTrue(String caseType);
}
