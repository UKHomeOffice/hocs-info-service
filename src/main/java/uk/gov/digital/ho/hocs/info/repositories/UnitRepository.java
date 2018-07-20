package uk.gov.digital.ho.hocs.info.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uk.gov.digital.ho.hocs.info.entities.Unit;

import java.util.List;

@Repository
public interface UnitRepository extends CrudRepository<Unit, Long> {

    @Query(value = "select * from unit u join tenant t on u.tenant_id = t.id where t.display_name = ?1", nativeQuery = true)
    List<Unit> findUnitByTenant(String tenant);

}
