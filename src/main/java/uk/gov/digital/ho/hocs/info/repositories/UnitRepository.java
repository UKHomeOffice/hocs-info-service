package uk.gov.digital.ho.hocs.info.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uk.gov.digital.ho.hocs.info.entities.Sla;
import uk.gov.digital.ho.hocs.info.entities.Unit;

import java.util.List;
import java.util.Set;

@Repository
public interface UnitRepository extends CrudRepository<Unit, String> {

    //@Query(value = "SELECT u.* FROM unit u JOIN unit_case_type uct ON u.uuid = uct.unit_uuid AND uct.case_type = ?1 AND u.active = TRUE", nativeQuery = true)
    @Query(value = "SELECT u.*, t.* FROM unit u JOIN team t ON u.uuid = t.unit_uuid JOIN unit_case_type uct ON u.uuid = uct.unit_uuid AND uct.case_type = ?1 AND u.active = TRUE", nativeQuery = true)
    Set<Unit> findAllActiveUnitsByCaseType(String caseType);

    Set<Unit> findAll();

    Unit findOneById(String df);

}
