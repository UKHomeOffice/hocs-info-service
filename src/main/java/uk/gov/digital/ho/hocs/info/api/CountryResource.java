package uk.gov.digital.ho.hocs.info.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.digital.ho.hocs.info.domain.model.Country;

import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@Slf4j
@RestController
public class CountryResource {

    private final CountryService countryService;

    @Autowired
    public CountryResource(CountryService countryService) {
        this.countryService = countryService;
    }

    @GetMapping(value = "/country", produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Set<String>> getAllActiveCountrys() {
        Set<Country> countrys = countryService.getAllActiveCountrys();
        return ResponseEntity.ok(countrys.stream().map(Country::getName).collect(Collectors.toSet()));
    }

    @GetMapping(value = "/countries/local", produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getAllActiveCountries() {

        log.info("getAllActiveCountries - local resource");
        File countries = null;
        try {
            countries = new ClassPathResource("countries-list.json").getFile();
        } catch (IOException e) {
            log.error("local countries file not found.");
             return ResponseEntity.badRequest().body("countries resource not found.");
        }
        return ResponseEntity.ok().body(new FileSystemResource(countries));
    }

    @GetMapping(value = "/territories/local", produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getAllActiveTerritories() {

        log.info("getAllActiveTerritories - local resource");
        File territories = null;
        try {
            territories = new ClassPathResource("territories-list.json").getFile();
        } catch (IOException e) {
            log.error("local territories file not found.");
            return ResponseEntity.badRequest().body("territories resource not found.");
        }
        return ResponseEntity.ok().body(new FileSystemResource(territories));
    }

    @GetMapping(value = "/admin/country/refresh")
    public ResponseEntity getFromApi() {
        countryService.updateWebCountryList();
        return ResponseEntity.ok("Success");
    }
}
