package uk.gov.digital.ho.hocs.info.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uk.gov.digital.ho.hocs.info.entities.CaseTypeDetail;

import java.util.List;

@Repository
public interface CaseTypeRepository extends CrudRepository<CaseTypeDetail, Long> {

    @Query(value = "select * from case_type c join tenant t on c.tenant_id = t.id where t.display_name = ?1", nativeQuery = true)
    List<CaseTypeDetail> findCaseTypesByTenant(String tenant);


}
