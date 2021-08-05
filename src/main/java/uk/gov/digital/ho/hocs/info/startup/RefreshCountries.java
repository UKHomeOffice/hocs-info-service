package uk.gov.digital.ho.hocs.info.startup;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import uk.gov.digital.ho.hocs.info.api.CountryService;

@Service
@Slf4j
public class RefreshCountries {

    private final CountryService countryService;

    @Autowired
    public RefreshCountries(CountryService countryService) {
        this.countryService = countryService;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void refreshCountries() {
        log.info("Refresh countries start");
        countryService.updateWebCountryList();
    }
}
