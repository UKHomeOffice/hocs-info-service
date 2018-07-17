package uk.gov.digital.ho.hocs.info.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import uk.gov.digital.ho.hocs.info.entities.CaseType;

import java.util.List;


public interface CaseTypeRepository extends CrudRepository<CaseType, Long> {

    @Query(value = "select * from case_type c join tenant t on c.tenant_id = t.id where t.display_name = ?1", nativeQuery = true)
    List<CaseType> findCaseTypesByTenant(String tenant);


}
