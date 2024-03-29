package uk.gov.digital.ho.hocs.info.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    public Configuration getConfiguration(String systemName) throws Exception {
        log.debug("Getting Configuration for {}", systemName);
        Configuration configuration = configurationRepository.findBySystemName(systemName);
        if (configuration.getProfiles() == null) {
            String errorMessage = "Empty Profiles";
            log.error(errorMessage);
            log.error(configuration.toString());
            throw new Exception(errorMessage);
        }
        if (configuration.getWorkstackTypes() == null) {
            String errorMessage = "Empty WorkstackTypes";
            log.error(errorMessage);
            log.error(configuration.toString());
            throw new Exception(errorMessage);
        } else {
            configuration.getWorkstackTypes().forEach(wst -> {
                if (wst.getWorkstackColumns() == null) {
                    String errorMessage = "Empty WorkstackColumns";
                    log.error(errorMessage);
                    log.error(configuration.toString());
                    throw new RuntimeException(errorMessage);
                }
            });
        }
        log.info("Got {} Configuration", configuration.getSystemName());
        return configuration;
    }

}
