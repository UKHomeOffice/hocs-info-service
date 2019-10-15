package uk.gov.digital.ho.hocs.info.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.digital.ho.hocs.info.client.ingest.ListConsumerService;
import uk.gov.digital.ho.hocs.info.domain.model.Country;
import uk.gov.digital.ho.hocs.info.domain.repository.CountryRepository;

import java.util.Set;

@Service
@Slf4j
public class CountryService {
    private final CountryRepository countryRepository;
    private final ListConsumerService listConsumerService;

    @Autowired
    public CountryService(CountryRepository countryRepository, ListConsumerService listConsumerService) {
        this.countryRepository = countryRepository;
        this.listConsumerService = listConsumerService;
    }

    public void updateWebCountryList() {
        log.info("Started Updating Countries List");
        updateCountry(listConsumerService.createFromCountryRegisterAPI());
        log.info("Finished Updating Countries List");
    }

    private void updateCountry(Set<Country> countrys) {
        countrys.forEach(country -> {
            log.debug("Looking for Country: {}", country.getName());
            Country countryFromDB = countryRepository.findByName(country.getName());
            if (countryFromDB != null) {
                log.info("Member {} found", country.getName());
                if (countryFromDB.getDeleted()) {
                    countryFromDB.setDeleted(false);
                    countryRepository.save(countryFromDB);
                }
            } else {
                log.info("Member {} not found, creating", country.getName());
                countryRepository.save(country);
            }
        });
    }
}
