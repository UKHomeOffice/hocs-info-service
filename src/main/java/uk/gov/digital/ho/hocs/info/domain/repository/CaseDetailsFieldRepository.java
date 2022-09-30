package uk.gov.digital.ho.hocs.info.domain.repository;

import org.springframework.data.repository.CrudRepository;
import uk.gov.digital.ho.hocs.info.domain.model.CaseDetailsField;

import java.util.List;

@Deprecated(forRemoval = true)
public interface CaseDetailsFieldRepository extends CrudRepository<CaseDetailsField, Long> {

    List<CaseDetailsField> findByCaseTypeOrderBySortOrder(String caseType);

}
