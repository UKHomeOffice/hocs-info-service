package uk.gov.digital.ho.hocs.info.domain.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.Set;
import java.util.UUID;

import uk.gov.digital.ho.hocs.info.domain.model.CorrespondentType;

@Repository
public interface CorrespondentTypeRepository extends CrudRepository<CorrespondentType, String> {

    Set<CorrespondentType> findAll();

    CorrespondentType findByUuid(UUID uuid);
}
