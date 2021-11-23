package uk.gov.digital.ho.hocs.info.domain.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uk.gov.digital.ho.hocs.info.domain.model.Configuration;

@Repository
public interface ConfigurationRepository extends CrudRepository<Configuration, String> {

    Configuration findBySystemName(String systemName);

}
