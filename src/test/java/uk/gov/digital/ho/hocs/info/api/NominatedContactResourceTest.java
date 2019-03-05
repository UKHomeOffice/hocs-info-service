package uk.gov.digital.ho.hocs.info.api;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uk.gov.digital.ho.hocs.info.api.dto.CreateNominatedContactDto;
import uk.gov.digital.ho.hocs.info.api.dto.UpdateNominatedContactDto;
import uk.gov.digital.ho.hocs.info.domain.model.NominatedContact;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class NominatedContactResourceTest {

    @Mock
    private NominatedContactService nominatedContactService;

    private NominatedContactResource nominatedContactResource;

    private static UUID teamUUID = UUID.randomUUID();
    private static String email = "email@test.com";

    @Before
    public void setUp() {
        nominatedContactResource = new NominatedContactResource(nominatedContactService);
    }

    @Test
    public void shouldReturnNominatedContactForRequestedTeam() {

        when(nominatedContactService.getNominatedContacts(teamUUID)).thenReturn(getMockNominatedContactsForTeam());

        ResponseEntity<Set<NominatedContact>> response =
                nominatedContactResource.getNominatedContacts(teamUUID);

        verify(nominatedContactService, times(1)).getNominatedContacts(teamUUID);
        verifyNoMoreInteractions(nominatedContactService);
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    private static Set<NominatedContact> getMockNominatedContactsForTeam() {
        Set<NominatedContact> NominatedContact = new HashSet<>();

        NominatedContact.add(new NominatedContact(teamUUID, email));
        NominatedContact.add(new NominatedContact(teamUUID, email));
        return NominatedContact;
    }

    @Test
    public void shouldCreateNominatedContactForRequestedTeam() {

        CreateNominatedContactDto request = new CreateNominatedContactDto(email);
        NominatedContact contact = new NominatedContact(teamUUID, email);
        when(nominatedContactService.createNominatedContact(teamUUID, email)).thenReturn(contact);

        ResponseEntity<UUID> response = nominatedContactResource.createNominatedContact(teamUUID, request);

        verify(nominatedContactService, times(1)).createNominatedContact(teamUUID, email);
        verifyNoMoreInteractions(nominatedContactService);
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    }

    @Test
    public void shouldUpdateRequestedNominatedContact() {

        UUID contactUUID = UUID.randomUUID();
        String newEmail = "new@test.com";

        UpdateNominatedContactDto request = new UpdateNominatedContactDto(newEmail);
        NominatedContact contact = new NominatedContact(contactUUID, newEmail);

        ResponseEntity<UUID> response = nominatedContactResource.updateNominatedContact(contactUUID, request);

        verify(nominatedContactService, times(1)).updateNominatedContact(contactUUID, newEmail);
        verifyNoMoreInteractions(nominatedContactService);
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    }

    @Test
    public void shouldDeleteRequestedNominatedContact() {

        UUID contactUUID = UUID.randomUUID();
        String newEmail = "new@test.com";

        NominatedContact contact = new NominatedContact(contactUUID, newEmail);

        ResponseEntity<UUID> response = nominatedContactResource.deleteNominatedContact(teamUUID, contactUUID);

        verify(nominatedContactService, times(1)).deleteNominatedContact(teamUUID, contactUUID);
        verifyNoMoreInteractions(nominatedContactService);
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    }
}