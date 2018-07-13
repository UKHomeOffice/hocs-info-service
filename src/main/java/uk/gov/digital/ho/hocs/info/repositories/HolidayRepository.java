package uk.gov.digital.ho.hocs.info.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import uk.gov.digital.ho.hocs.info.entities.Holiday;

import java.util.List;

public interface HolidayRepository extends CrudRepository<Holiday, Long> {

    @Query(value ="select h.* from tenants_holidays th join holiday h on h.id = th.holiday_id join tenant t on t.id = th.tenant_id join case_type ct on t.id = ct.tenant_id where ct.display_name = ?1", nativeQuery = true )
    List<Holiday> findAllByCaseType(String caseTypeDisplayName);

}
