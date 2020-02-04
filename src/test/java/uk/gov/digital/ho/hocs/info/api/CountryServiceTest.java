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
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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

        Set<Country> countrys = new HashSet<>();
        countrys.add(new Country(1L, "country", false, false));
        when(listConsumerService.createFromCountryRegisterAPI()).thenReturn(countrys);
        Set<Country> territorys = new HashSet<>();
        territorys.add(new Country(1L, "territory", true, false));
        when(listConsumerService.createFromTerritoryRegisterAPI()).thenReturn(territorys);

        countryService.updateWebCountryList();

        verify(countryRepository, times(1)).deleteAll();
        verify(countryRepository, times(1)).findByName("country");
        verify(countryRepository, times(1)).findByName("territory");
        verify(countryRepository, times(1)).findByName("Unknown");
        verify(countryRepository, times(1)).findByName("Netherlands Antilles");
        verify(countryRepository, times(4)).save(any(Country.class));
        verifyNoMoreInteractions(countryRepository);
    }

    @Test
    public void shouldUpdateWebCountryListWithCountry() {

        ArgumentCaptor<Country> argument = ArgumentCaptor.forClass(Country.class);
        Set<Country> countrys = new HashSet<>();
        countrys.add(new Country(1L, "country", false, false));
        when(listConsumerService.createFromCountryRegisterAPI()).thenReturn(countrys);

        countryService.updateWebCountryList();

        verify(countryRepository, times(1)).deleteAll();
        verify(countryRepository, times(1)).findByName("country");
        verify(countryRepository, times(1)).findByName("Unknown");
        verify(countryRepository, times(1)).findByName("Netherlands Antilles");

        verify(countryRepository, times(3)).save(any(Country.class));
        verify(countryRepository, times(3)).save(argument.capture());

        List<Country> capturedCountries = argument.getAllValues();
        assertThat(capturedCountries.size()).isEqualTo(3);
        assertThat(capturedCountries.get(0).getName()).isEqualTo("country");
        assertThat(capturedCountries.get(0).getIsTerritory()).isEqualTo(Boolean.FALSE);
        assertThat(capturedCountries.get(0).getDeleted()).isEqualTo(Boolean.FALSE);
        assertThat(capturedCountries.get(1).getName()).isEqualTo("Unknown");
        assertThat(capturedCountries.get(1).getIsTerritory()).isEqualTo(Boolean.TRUE);
        assertThat(capturedCountries.get(1).getDeleted()).isEqualTo(Boolean.FALSE);
        assertThat(capturedCountries.get(2).getName()).isEqualTo("Netherlands Antilles");
        assertThat(capturedCountries.get(2).getIsTerritory()).isEqualTo(Boolean.FALSE);
        assertThat(capturedCountries.get(2).getDeleted()).isEqualTo(Boolean.FALSE);

        verifyNoMoreInteractions(countryRepository);
    }

    @Test
    public void shouldUpdateWebCountryListWithTerritory() {

        ArgumentCaptor<Country> argument = ArgumentCaptor.forClass(Country.class);
        Set<Country> territorys = new HashSet<>();
        territorys.add(new Country(1L, "territory", true, false));
        when(listConsumerService.createFromTerritoryRegisterAPI()).thenReturn(territorys);

        countryService.updateWebCountryList();

        verify(countryRepository, times(1)).deleteAll();
        verify(countryRepository, times(1)).findByName("territory");
        verify(countryRepository, times(1)).findByName("Unknown");
        verify(countryRepository, times(1)).findByName("Netherlands Antilles");
        verify(countryRepository, times(3)).save(any(Country.class));
        verify(countryRepository, times(3)).save(argument.capture());

        List<Country> capturedCountries = argument.getAllValues();
        assertThat(capturedCountries.size()).isEqualTo(3);
        assertThat(capturedCountries.get(0).getName()).isEqualTo("territory");
        assertThat(capturedCountries.get(0).getIsTerritory()).isEqualTo(Boolean.TRUE);
        assertThat(capturedCountries.get(0).getDeleted()).isEqualTo(Boolean.FALSE);
        assertThat(capturedCountries.get(1).getName()).isEqualTo("Unknown");
        assertThat(capturedCountries.get(1).getIsTerritory()).isEqualTo(Boolean.TRUE);
        assertThat(capturedCountries.get(1).getDeleted()).isEqualTo(Boolean.FALSE);
        assertThat(capturedCountries.get(2).getName()).isEqualTo("Netherlands Antilles");
        assertThat(capturedCountries.get(2).getIsTerritory()).isEqualTo(Boolean.FALSE);
        assertThat(capturedCountries.get(2).getDeleted()).isEqualTo(Boolean.FALSE);
        verifyNoMoreInteractions(countryRepository);
    }
}
