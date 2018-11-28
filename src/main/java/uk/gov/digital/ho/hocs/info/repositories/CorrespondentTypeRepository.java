package uk.gov.digital.ho.hocs.info.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.Set;
import uk.gov.digital.ho.hocs.info.entities.CorrespondentType;

@Repository
public interface CorrespondentTypeRepository extends CrudRepository<CorrespondentType, String> {

    Set<CorrespondentType> findAll();
}
