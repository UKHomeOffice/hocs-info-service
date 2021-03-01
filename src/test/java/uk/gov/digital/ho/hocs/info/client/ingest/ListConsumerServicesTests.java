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
import uk.gov.digital.ho.hocs.info.domain.model.HouseAddress;
import uk.gov.digital.ho.hocs.info.domain.model.Member;
import uk.gov.digital.ho.hocs.info.domain.repository.HouseAddressRepository;

import java.util.Set;

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
                        "Test5",
                        "Test6",
                        houseAddressRepository,
                        restTemplate);
    }

    @Test(expected = ApplicationExceptions.IngestException.class)
    public void getDataFromAPI_whenRestTemplateThrowsException_handlesException() {
        when(houseAddressRepository.findByHouseCode(any())).thenReturn(new HouseAddress());
        when(restTemplate.exchange(eq("Test3"), eq(HttpMethod.GET), any(), eq(IrishMembers.class)))
                .thenThrow(new RestClientException("Test"));

        listConsumerService.createFromIrishAssemblyAPI();
    }

    @Test
    public void retrieveHouseAddress_whenRepoReturnsNull_shouldReturnEmptyCollection() {
        when(houseAddressRepository.findByHouseCode(any())).thenReturn(null);

        Set<Member> members = listConsumerService.createFromIrishAssemblyAPI();

        verify(houseAddressRepository).findByHouseCode(any());

        assert(members).isEmpty();
    }
}
