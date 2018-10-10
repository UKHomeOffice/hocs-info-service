package uk.gov.digital.ho.hocs.info.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uk.gov.digital.ho.hocs.info.entities.Minister;

import java.util.Set;


@Repository
public interface MinisterRepository extends CrudRepository<Minister, String> {

    Set<Minister> findAll();

}
