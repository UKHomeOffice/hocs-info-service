package uk.gov.digital.ho.hocs.info.client.ingest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import uk.gov.digital.ho.hocs.info.domain.exception.ApplicationExceptions;
import uk.gov.digital.ho.hocs.info.domain.model.Country;
import uk.gov.digital.ho.hocs.info.domain.model.HouseAddress;
import uk.gov.digital.ho.hocs.info.domain.model.Member;
import uk.gov.digital.ho.hocs.info.domain.repository.HouseAddressRepository;

import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ListConsumerServicesTests {

    @Mock
    private HouseAddressRepository houseAddressRepository;

    @Mock
    private RestTemplate restTemplate;


    private ListConsumerService listConsumerService;

    @Before
    public void setUp() {
        this.listConsumerService =
                new ListConsumerService("Test1",
                        "Test2",
                        "Test3",
                        "Test4",
                        "countries-list.json",
                        "territories-list.json",
                        houseAddressRepository,
                        restTemplate);
    }

    @Test
    public void getDataFromAPI_whenRestTemplateThrowsException_shouldReturnEmptyCollection() {
        when(houseAddressRepository.findByHouseCode(any())).thenReturn(new HouseAddress());
        when(restTemplate.exchange(eq("Test3"), eq(HttpMethod.GET), any(), eq(IrishMembers.class)))
                .thenThrow(new RestClientException("Test"));

        Set<Member> members = listConsumerService.createFromIrishAssemblyAPI();

        assertTrue(members.isEmpty());
    }

    @Test
    public void retrieveHouseAddress_whenRepoReturnsNull_shouldReturnEmptyCollection() {
        when(houseAddressRepository.findByHouseCode(any())).thenReturn(null);

        Set<Member> members = listConsumerService.createFromIrishAssemblyAPI();

        verify(houseAddressRepository).findByHouseCode(any());

        assertTrue(members.isEmpty());
    }

    @Test
    public void shouldCreateCountryListFromFile() {
        Country expected = new Country("Gabon", true);
        Set<Country> countries = listConsumerService.createFromCountryFile();
        assertEquals(199, countries.size());
        assertTrue(countries.contains(expected));
    }

    @Test
    public void shouldCreateTerritoryListFromFile() {
        Country expected = new Country("Falkland Islands", false);
        Set<Country> countries = listConsumerService.createFromTerritoryFile();
        assertEquals(79, countries.size());
        assertTrue(countries.contains(expected));
    }
}
