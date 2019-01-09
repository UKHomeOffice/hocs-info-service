package uk.gov.digital.ho.hocs.info.api;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uk.gov.digital.ho.hocs.info.api.NominatedContactResource;
import uk.gov.digital.ho.hocs.info.api.dto.GetNominatedContactResponse;
import uk.gov.digital.ho.hocs.info.domain.model.NominatedContact;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class NominatedContactResourceTest {

    @Mock
    private uk.gov.digital.ho.hocs.info.api.NominatedContactService NominatedContactService;

    private uk.gov.digital.ho.hocs.info.api.NominatedContactResource NominatedContactResource;

    private static UUID teamUUID = UUID.randomUUID();

    @Before
    public void setUp() {
        NominatedContactResource = new NominatedContactResource(NominatedContactService);
    }

    @Test
    public void shouldReturnNominatedContactForRequestedTeam() {

        when(NominatedContactService.getNominatedContact(teamUUID)).thenReturn(getMockNominatedContactForTeam());

        ResponseEntity<GetNominatedContactResponse> response =
                NominatedContactResource.getNominatedContact(teamUUID);

        verify(NominatedContactService, times(1)).getNominatedContact(teamUUID);
        verifyNoMoreInteractions(NominatedContactService);
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    private static Set<NominatedContact> getMockNominatedContactForTeam() {
        Set<NominatedContact> NominatedContact = new HashSet<>();

        NominatedContact.add(new NominatedContact(1l, teamUUID, "test@test.com"));
        NominatedContact.add(new NominatedContact(2l, teamUUID, "test@test.com"));
        return NominatedContact;

    }
}