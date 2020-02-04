package uk.gov.digital.ho.hocs.info.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import uk.gov.digital.ho.hocs.info.client.ingest.ListConsumerService;
import uk.gov.digital.ho.hocs.info.domain.model.Country;
import uk.gov.digital.ho.hocs.info.domain.repository.CountryRepository;

import java.util.Collection;
import java.util.List;
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

    @Cacheable(value = "ActiveCountrys", unless = "#result == null or #result.size() == 0")
    public Set<Country> getAllActiveCountrys() {
        log.debug("Getting all active countries");
        Set<Country> countrys = countryRepository.findAllActiveCountrys();
        log.info("Got {} countries", countrys.size());
        return countrys;
    }

    @CacheEvict(value = "ActiveCountrys")
    public void updateWebCountryList() {
        log.info("Started Updating Countries/Territories List");
        countryRepository.deleteAll();
        updateCountry(listConsumerService.createFromCountryRegisterAPI());
        updateCountry(listConsumerService.createFromTerritoryRegisterAPI());
        List<Country> customEntries = List.of(
                new Country("Unknown", true),
                new Country("Netherlands Antilles", false));
        updateCountry(customEntries);
        log.info("Finished Updating Countries/Territories List");
    }

    private void updateCountry(Collection<Country> countrys) {
        countrys.forEach(country -> {
            log.debug("Looking for Country: {}", country.getName());
            Country countryFromDB = countryRepository.findByName(country.getName());
            if (countryFromDB != null) {
                log.info("Country {} found, updating", country.getName());
                countryFromDB.setIsTerritory(country.getIsTerritory());
                countryFromDB.setDeleted(false);
                countryRepository.save(countryFromDB);
            } else {
                log.info("Country {} not found, creating", country.getName());
                countryRepository.save(country);
            }
        });
    }
}
