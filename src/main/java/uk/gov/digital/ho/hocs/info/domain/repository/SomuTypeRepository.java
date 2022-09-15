package uk.gov.digital.ho.hocs.info.domain.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uk.gov.digital.ho.hocs.info.domain.model.SomuType;

import java.util.Set;

@Repository
public interface SomuTypeRepository extends CrudRepository<SomuType, String> {

    SomuType findByCaseTypeAndType(String caseType, String type);

    Set<SomuType> findAll();

    Set<SomuType> findAllByCaseType(String caseType);

}
