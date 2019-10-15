package uk.gov.digital.ho.hocs.info.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CountryResource {

    private final CountryService countryService;

    @Autowired
    public CountryResource(CountryService countryService) {
        this.countryService = countryService;
    }

    @GetMapping(value = "/admin/country/refresh")
    public ResponseEntity getFromApi() {
        countryService.updateWebCountryList();
        return ResponseEntity.ok("Success");
    }
}
