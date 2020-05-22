package uk.gov.digital.ho.hocs.info.domain.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.Set;
import java.util.UUID;

import uk.gov.digital.ho.hocs.info.domain.model.CorrespondentType;

@Repository
public interface CorrespondentTypeRepository extends CrudRepository<CorrespondentType, String> {

    Set<CorrespondentType> findAll();

    @Query(value = "SELECT cor.* FROM correspondent_type cor INNER JOIN case_type_correspondent_type ctct ON ctct.correspondent_type_uuid=cor.uuid INNER JOIN case_type cas ON cas.uuid = ctct.case_type_uuid WHERE cas.type = ?1 AND cas.active = TRUE", nativeQuery = true)
    Set<CorrespondentType> findAllByCaseType(String caseType);

    CorrespondentType findByUuid(UUID uuid);
}
