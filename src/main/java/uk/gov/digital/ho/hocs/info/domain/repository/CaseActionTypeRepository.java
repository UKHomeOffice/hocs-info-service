package uk.gov.digital.ho.hocs.info.domain.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uk.gov.digital.ho.hocs.info.domain.model.CaseActionType;

import java.util.List;

@Repository
public interface CaseActionTypeRepository extends CrudRepository<CaseActionType, Long> {

    List<CaseActionType> findAllByCaseTypeAndActiveIsTrue(String caseType);
}
