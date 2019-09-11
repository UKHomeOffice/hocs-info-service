package uk.gov.digital.ho.hocs.info.api;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uk.gov.digital.ho.hocs.info.api.dto.*;
import uk.gov.digital.ho.hocs.info.domain.model.Constituency;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@RunWith(MockitoJUnitRunner.class)
public class ConstituencyResourceTest {

    @Mock
    private ConstituencyService constituencyService;

    private ConstituencyResource constituencyResource;

    @Before
    public void setUp() {
        constituencyResource = new ConstituencyResource(constituencyService);
    }

    @Test
    public void shouldGetAllConstituencys() {
        when(constituencyService.getConstituencys()).thenReturn(getConstituencys());

        ResponseEntity<Set<ConstituencyDto>> response = constituencyResource.getConstituencys();

        verify(constituencyService, times(1)).getConstituencys();
        verifyNoMoreInteractions(constituencyService);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void shouldGetConstituencysByCaseType() {
        when(constituencyService.getConstituencyList("MIN")).thenReturn(getConstituencys());

        ResponseEntity<GetAllConstituencysResponse> response = constituencyResource.getAllConstituencysByCaseType("MIN");

        verify(constituencyService, times(1)).getConstituencyList("MIN");
        verifyNoMoreInteractions(constituencyService);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void getConstituencyByUUID() {
        when(constituencyService.getConstituency(any())).thenReturn(new Constituency("Constituency1"));

        ResponseEntity<ConstituencyDto> response = constituencyResource.getConstituencyByUUID(UUID.randomUUID());

        verify(constituencyService, times(1)).getConstituency(any());
        verifyNoMoreInteractions(constituencyService);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void getConstituencyByMemberExternalReference() {
        when(constituencyService.getConstituencyByMemberExternalReference("extRef")).thenReturn(new Constituency("Constituency1"));

        ResponseEntity<ConstituencyDto> response = constituencyResource.getConstituencyByMemberExternalReference("extRef");

        verify(constituencyService, times(1)).getConstituencyByMemberExternalReference("extRef");
        verifyNoMoreInteractions(constituencyService);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    private List<Constituency> getConstituencys() {
        return new ArrayList<Constituency>() {{
            add(new Constituency("Constituency1"));
            add(new Constituency("Constituency2"));
        }};
    }

    @Test
    public void shouldCreateConstituency() {

        CreateConstituencyDto request = new CreateConstituencyDto("ParentConstituency");

        when(constituencyService.createConstituency(any())).thenReturn(UUID.randomUUID());

        ResponseEntity<CreateConstituencyResponse> response = constituencyResource.createConstituency(request);

        verify(constituencyService, times(1)).createConstituency(any());
        verifyNoMoreInteractions(constituencyService);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void shouldDeleteConstituency() {

        ResponseEntity response = constituencyResource.deleteConstituency(UUID.randomUUID());

        verify(constituencyService, times(1)).deleteConstituency(any());
        verifyNoMoreInteractions(constituencyService);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void shouldReactivateConstituency() {

        ResponseEntity response = constituencyResource.reactivateConstituency(UUID.randomUUID());

        verify(constituencyService, times(1)).reactivateConstituency(any());
        verifyNoMoreInteractions(constituencyService);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
