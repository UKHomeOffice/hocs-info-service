package uk.gov.digital.ho.hocs.info.domain.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uk.gov.digital.ho.hocs.info.domain.model.Tenant;

@Repository
public interface TenantRepository extends CrudRepository<Tenant, String> {
}
