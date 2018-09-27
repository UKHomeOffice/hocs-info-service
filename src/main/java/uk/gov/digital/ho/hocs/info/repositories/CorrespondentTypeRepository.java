package uk.gov.digital.ho.hocs.info.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uk.gov.digital.ho.hocs.info.entities.CorrespondentType;

import java.util.Set;

@Repository
public interface CorrespondentTypeRepository extends CrudRepository<CorrespondentType, String> {

    Set<CorrespondentType> findAll();
}
