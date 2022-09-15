package uk.gov.digital.ho.hocs.info.domain.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uk.gov.digital.ho.hocs.info.domain.model.Unit;

import java.util.Set;
import java.util.UUID;

@Repository
public interface UnitRepository extends CrudRepository<Unit, Long> {

    Set<Unit> findAll();

    Unit findByUuid(UUID uuid);

}
