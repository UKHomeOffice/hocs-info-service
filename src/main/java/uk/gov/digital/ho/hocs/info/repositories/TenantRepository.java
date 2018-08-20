package uk.gov.digital.ho.hocs.info.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uk.gov.digital.ho.hocs.info.entities.Tenant;

@Repository
public interface TenantRepository extends CrudRepository<Tenant, String> {
}
