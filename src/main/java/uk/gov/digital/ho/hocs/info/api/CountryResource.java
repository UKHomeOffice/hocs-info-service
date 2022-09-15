package uk.gov.digital.ho.hocs.info.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.digital.ho.hocs.info.domain.model.Country;

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

    @GetMapping(value = "/admin/country/refresh")
    public ResponseEntity<String> getFromApi() {
        countryService.updateWebCountryList();
        return ResponseEntity.ok("Success");
    }

}
