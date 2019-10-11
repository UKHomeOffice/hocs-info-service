package uk.gov.digital.ho.hocs.info.api;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uk.gov.digital.ho.hocs.info.api.dto.GetMembersResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;

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
    public void shouldGetFromAPI() {

        doNothing().when(countryService).updateWebCountryList();

        ResponseEntity<GetMembersResponse> response = countryResource.getFromApi();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(countryService, times(1)).updateWebCountryList();
        verifyNoMoreInteractions(countryService);
    }
}
