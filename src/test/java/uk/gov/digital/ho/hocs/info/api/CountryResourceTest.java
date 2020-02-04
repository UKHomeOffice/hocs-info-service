package uk.gov.digital.ho.hocs.info.api;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uk.gov.digital.ho.hocs.info.api.dto.GetMembersResponse;
import uk.gov.digital.ho.hocs.info.domain.model.Country;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CountryResourceTest {

    @Mock
    private CountryService countryService;

    private CountryResource countryResource;

    @Before
    public void setUp() {
        countryResource = new CountryResource(countryService);
    }

    @Test
    public void shouldGetAllActiveCountrys() {
        Set<Country> countrys = new HashSet<Country>();
        countrys.add(new Country("testCountry", false));
        when(countryService.getAllActiveCountrys()).thenReturn(countrys);

        ResponseEntity<Set<String>> response = countryResource.getAllActiveCountrys();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().size()).isEqualTo(1);
        verify(countryService, times(1)).getAllActiveCountrys();
        verifyNoMoreInteractions(countryService);
    }

    @Test
    public void shouldGetFromAPI() {

        doNothing().when(countryService).updateWebCountryList();

        ResponseEntity<GetMembersResponse> response = countryResource.getFromApi();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(countryService, times(1)).updateWebCountryList();
        verifyNoMoreInteractions(countryService);
    }
}
