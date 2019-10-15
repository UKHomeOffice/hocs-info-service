package uk.gov.digital.ho.hocs.info.api;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.digital.ho.hocs.info.client.ingest.ListConsumerService;
import uk.gov.digital.ho.hocs.info.domain.model.Country;
import uk.gov.digital.ho.hocs.info.domain.repository.CountryRepository;

import java.util.HashSet;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@RunWith(MockitoJUnitRunner.class)
public class CountryServiceTest {

    @Mock
    private CountryRepository countryRepository;

    @Mock
    private ListConsumerService listConsumerService;

    private CountryService countryService;

    @Before
    public void setUp() {
        this.countryService = new CountryService(countryRepository, listConsumerService);
    }

    @Test
    public void shouldUpdateWebCountryList() {

        Set<Country> countrys = new HashSet<Country>();
        countrys.add(new Country(1L, "name", false));
        when(listConsumerService.createFromCountryRegisterAPI()).thenReturn(countrys);

        countryService.updateWebCountryList();

        verify(countryRepository, times(1)).findByName("name");
        verify(countryRepository, times(1)).save(any());
        verifyNoMoreInteractions(countryRepository);
    }
}
