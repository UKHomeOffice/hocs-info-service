package uk.gov.digital.ho.hocs.info.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.gov.digital.ho.hocs.info.api.dto.ConfigurationDto;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
public class ConfigurationResource {

    private final ConfigurationService configurationService;

    @Autowired
    public ConfigurationResource(ConfigurationService configurationService) {
        this.configurationService = configurationService;
    }

    @GetMapping(value = "/configuration", produces = APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity<ConfigurationDto> getConfiguration() {
        // configuration name is hardcoded currently, but this leaves it open for extension if we wanted to deploy
        // multiple frontends implementations in single environment
        return ResponseEntity.ok(ConfigurationDto.from(configurationService.getConfiguration("system")));
    }
}
