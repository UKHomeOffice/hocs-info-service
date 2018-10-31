package uk.gov.digital.ho.hocs.info.nominatedPerson;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uk.gov.digital.ho.hocs.info.dto.GetNominatedPeopleResponse;
import uk.gov.digital.ho.hocs.info.entities.NominatedPerson;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class NominatedPeopleResourceTest {

    @Mock
    private NominatedPeopleService nominatedPeopleService;

    private NominatedPeopleResource nominatedPeopleResource;

    private static UUID teamUUID = UUID.randomUUID();

    @Before
    public void setUp() {
        nominatedPeopleResource = new NominatedPeopleResource(nominatedPeopleService);
    }

    @Test
    public void shouldReturnNominatedPeopleForRequestedTeam() {

        when(nominatedPeopleService.getNominatedPerson(teamUUID)).thenReturn(getMockNominatedPeopleForTeam());

        ResponseEntity<GetNominatedPeopleResponse> response =
                nominatedPeopleResource.getNominatedPerson(teamUUID);

        verify(nominatedPeopleService, times(1)).getNominatedPerson(teamUUID);
        verifyNoMoreInteractions(nominatedPeopleService);
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    private static Set<NominatedPerson> getMockNominatedPeopleForTeam() {
        Set<NominatedPerson> nominatedPeople = new HashSet<>();

        nominatedPeople.add(new NominatedPerson(1l, teamUUID, "test@test.com"));
        nominatedPeople.add(new NominatedPerson(2l, teamUUID, "test@test.com"));
        return nominatedPeople;

    }
}