package uk.gov.digital.ho.hocs.info.api;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.digital.ho.hocs.info.client.ingest.ListConsumerService;
import uk.gov.digital.ho.hocs.info.domain.model.Country;
import uk.gov.digital.ho.hocs.info.domain.repository.CountryRepository;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
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
    public void shouldReturnAllActiveMembers() {

        countryService.getAllActiveCountrys();

        verify(countryRepository, times(1)).findAllActiveCountrys();
        verifyNoMoreInteractions(countryRepository);
    }

    @Test
    public void shouldUpdateWebCountryList() {

        Set<Country> countrys = new HashSet<Country>();
        countrys.add(new Country(1L, "country", false, false));
        when(listConsumerService.createFromCountryRegisterAPI()).thenReturn(countrys);
        Set<Country> territorys = new HashSet<Country>();
        territorys.add(new Country(1L, "territory", true, false));
        when(listConsumerService.createFromTerritoryRegisterAPI()).thenReturn(territorys);

        countryService.updateWebCountryList();

        verify(countryRepository, times(1)).deleteAll();
        verify(countryRepository, times(1)).findByName("country");
        verify(countryRepository, times(1)).findByName("territory");
        verify(countryRepository, times(2)).save(any(Country.class));
        verifyNoMoreInteractions(countryRepository);
    }

    @Test
    public void shouldUpdateWebCountryListWithCountry() {

        ArgumentCaptor<Country> argument = ArgumentCaptor.forClass(Country.class);
        Set<Country> countrys = new HashSet<Country>();
        countrys.add(new Country(1L, "country", false, false));
        when(listConsumerService.createFromCountryRegisterAPI()).thenReturn(countrys);

        countryService.updateWebCountryList();

        verify(countryRepository, times(1)).deleteAll();
        verify(countryRepository, times(1)).findByName("country");
        verify(countryRepository, times(1)).save(any(Country.class));
        verify(countryRepository).save(argument.capture());
        assertThat(argument.getValue().getName()).isEqualTo("country");
        assertThat(argument.getValue().getIsTerritory()).isEqualTo(Boolean.FALSE);
        assertThat(argument.getValue().getDeleted()).isEqualTo(Boolean.FALSE);
        verifyNoMoreInteractions(countryRepository);
    }

    @Test
    public void shouldUpdateWebCountryListWithTerritory() {

        ArgumentCaptor<Country> argument = ArgumentCaptor.forClass(Country.class);
        Set<Country> territorys = new HashSet<Country>();
        territorys.add(new Country(1L, "territory", true, false));
        when(listConsumerService.createFromTerritoryRegisterAPI()).thenReturn(territorys);

        countryService.updateWebCountryList();

        verify(countryRepository, times(1)).deleteAll();
        verify(countryRepository, times(1)).findByName("territory");
        verify(countryRepository, times(1)).save(any(Country.class));
        verify(countryRepository).save(argument.capture());
        assertThat(argument.getValue().getName()).isEqualTo("territory");
        assertThat(argument.getValue().getIsTerritory()).isEqualTo(Boolean.TRUE);
        assertThat(argument.getValue().getDeleted()).isEqualTo(Boolean.FALSE);
        verifyNoMoreInteractions(countryRepository);
    }
}
