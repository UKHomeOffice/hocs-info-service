package uk.gov.digital.ho.hocs.info.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uk.gov.digital.ho.hocs.info.entities.Unit;

import java.util.Set;
import java.util.UUID;

@Repository
public interface UnitRepository extends CrudRepository<Unit, Long> {

    @Query(value = "SELECT u.*, t.* FROM unit u JOIN team t ON u.uuid = t.unit_uuid JOIN unit_case_type uct ON u.uuid = uct.unit_uuid AND uct.case_type = ?1 AND u.active = TRUE", nativeQuery = true)
    Set<Unit> findAllActiveUnitsByCaseType(String caseType);
    Set<Unit> findAll();
    Unit findByUuid(UUID uuid);
}
