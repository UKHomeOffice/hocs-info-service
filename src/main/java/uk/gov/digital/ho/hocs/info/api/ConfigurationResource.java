package uk.gov.digital.ho.hocs.info.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import uk.gov.digital.ho.hocs.info.api.dto.ConfigurationDto;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@Slf4j
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
        try {
            return ResponseEntity.ok(ConfigurationDto.from(configurationService.getConfiguration("system")));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
