package uk.gov.digital.ho.hocs.info.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import uk.gov.digital.ho.hocs.info.domain.model.Configuration;
import uk.gov.digital.ho.hocs.info.domain.repository.ConfigurationRepository;

@Service
@Slf4j
public class ConfigurationService {

    private final ConfigurationRepository configurationRepository;

    @Autowired
    public ConfigurationService(ConfigurationRepository configurationRepository) {
        this.configurationRepository = configurationRepository;
    }

    @Cacheable("configuration")
    public Configuration getConfiguration(String systemName) {
        log.debug("Getting Configuration for {}", systemName);
        Configuration configuration = configurationRepository.findBySystemName(systemName);
        log.info("Got {} Configuration", configuration.getSystemName());
        return configuration;
    }

}
