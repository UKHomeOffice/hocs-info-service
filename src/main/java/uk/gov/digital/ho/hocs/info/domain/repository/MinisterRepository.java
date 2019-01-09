package uk.gov.digital.ho.hocs.info.domain.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uk.gov.digital.ho.hocs.info.domain.model.Minister;

import java.util.Set;


@Repository
public interface MinisterRepository extends CrudRepository<Minister, String> {

    Set<Minister> findAll();

}
