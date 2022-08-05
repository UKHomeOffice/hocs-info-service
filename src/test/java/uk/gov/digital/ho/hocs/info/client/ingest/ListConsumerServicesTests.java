package uk.gov.digital.ho.hocs.info.client.ingest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import uk.gov.digital.ho.hocs.info.domain.model.Country;
import uk.gov.digital.ho.hocs.info.domain.model.HouseAddress;
import uk.gov.digital.ho.hocs.info.domain.model.Member;
import uk.gov.digital.ho.hocs.info.domain.repository.HouseAddressRepository;

import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

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
        when(restTemplate.exchange(eq("Test3"), eq(HttpMethod.GET), any(), eq(NorthernIrishMembers.class)))
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
    public void whenNiAssemblyApiReturnsNoMembers_shouldReturnEmptyCollection() {
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);

        NorthernIrishMembers northernIrishMembers = new NorthernIrishMembers();

        ResponseEntity<?> responseEntity = new ResponseEntity<>(
                northernIrishMembers,
                header,
                HttpStatus.OK
        );

        when(houseAddressRepository.findByHouseCode(any())).thenReturn(new HouseAddress());
        when(restTemplate.exchange(eq("Test3"), eq(HttpMethod.GET), any(), eq(NorthernIrishMembers.class)))
                .thenReturn((ResponseEntity<NorthernIrishMembers>) responseEntity);

        Set<Member> members = listConsumerService.createFromIrishAssemblyAPI();

        assertTrue(members.isEmpty());
    }

    @Test
    public void whenCommonsApiReturnsNoMembers_shouldReturnEmptyCollection() {
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);

        UKMembers ukMembers = new UKMembers();

        ResponseEntity<?> responseEntity = new ResponseEntity<>(
                ukMembers,
                header,
                HttpStatus.OK
        );

        when(houseAddressRepository.findByHouseCode(any())).thenReturn(new HouseAddress());
        when(restTemplate.exchange(eq("Test1"), eq(HttpMethod.GET), any(), eq(UKMembers.class)))
                .thenReturn((ResponseEntity<UKMembers>) responseEntity);

        Set<Member> members = listConsumerService.createCommonsFromUKParliamentAPI();

        assertTrue(members.isEmpty());
    }

    @Test
    public void whenLordsApiReturnsNoMembers_shouldReturnEmptyCollection() {
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);

        UKMembers ukMembers = new UKMembers();

        ResponseEntity<?> responseEntity = new ResponseEntity<>(
                ukMembers,
                header,
                HttpStatus.OK
        );

        when(houseAddressRepository.findByHouseCode(any())).thenReturn(new HouseAddress());
        when(restTemplate.exchange(eq("Test1"), eq(HttpMethod.GET), any(), eq(UKMembers.class)))
                .thenReturn((ResponseEntity<UKMembers>) responseEntity);

        Set<Member> members = listConsumerService.createLordsFromUKParliamentAPI();

        assertTrue(members.isEmpty());
    }

    @Test
    public void whenWelshParliamentApiReturnsNoMembers_shouldReturnEmptyCollection() {
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);

        WelshWards welshWards = new WelshWards();

        ResponseEntity<?> responseEntity = new ResponseEntity<>(
                welshWards,
                header,
                HttpStatus.OK
        );

        when(houseAddressRepository.findByHouseCode(any())).thenReturn(new HouseAddress());
        when(restTemplate.exchange(eq("Test4"), eq(HttpMethod.GET), any(), eq(WelshWards.class)))
                .thenReturn((ResponseEntity<WelshWards>) responseEntity);

        Set<Member> members = listConsumerService.createFromWelshParliamentAPI();

        assertTrue(members.isEmpty());
    }

    @Test
    public void whenScottishParliamentApiReturnsNoMembers_shouldReturnEmptyCollection() {
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);

        ScottishMember[] scottishMembers = new ScottishMember[0];

        ResponseEntity<?> responseEntity = new ResponseEntity<>(
                scottishMembers,
                header,
                HttpStatus.OK
        );

        when(houseAddressRepository.findByHouseCode(any())).thenReturn(new HouseAddress());
        when(restTemplate.exchange(eq("Test2"), eq(HttpMethod.GET), any(), eq(ScottishMember[].class)))
                .thenReturn((ResponseEntity<ScottishMember[]>) responseEntity);

        Set<Member> members = listConsumerService.createFromScottishParliamentAPI();

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
