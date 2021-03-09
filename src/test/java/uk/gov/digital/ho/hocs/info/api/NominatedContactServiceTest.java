package uk.gov.digital.ho.hocs.info.api;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.dao.DataIntegrityViolationException;
import uk.gov.digital.ho.hocs.info.domain.exception.ApplicationExceptions;
import uk.gov.digital.ho.hocs.info.domain.model.NominatedContact;
import uk.gov.digital.ho.hocs.info.domain.repository.NominatedContactRepository;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class NominatedContactServiceTest {

    @Mock
    private NominatedContactRepository nominatedContactRepository;

    private NominatedContactService nominatedContactService;

    private static UUID teamUUID = UUID.randomUUID();
    private static String emailAddress = "email@test.com";

    @Before
    public void setUp() {
        this.nominatedContactService = new NominatedContactService(nominatedContactRepository);
    }

    @Test
    public void shouldReturnNominatedContacts() {

        Set<NominatedContact> contacts = new HashSet<NominatedContact>();
        contacts.add(new NominatedContact(teamUUID, "one@email.com"));
        contacts.add(new NominatedContact(teamUUID, "two@email.com"));

        when(nominatedContactRepository.findAllByTeamUUID(teamUUID)).thenReturn(contacts);

        Set<NominatedContact> NominatedContact = nominatedContactService.getNominatedContacts(teamUUID);

        verify(nominatedContactRepository, times(1)).findAllByTeamUUID(teamUUID);
        verifyNoMoreInteractions(nominatedContactRepository);
        assertThat(NominatedContact.size()).isEqualTo(2);
    }

    @Test
    public void shouldCreateNominatedContact() {

        NominatedContact contact = nominatedContactService.createNominatedContact(teamUUID, emailAddress);

        assertThat(contact.getUuid()).isNotNull();
        verify(nominatedContactRepository, times(1)).save(any(NominatedContact.class));
        verifyNoMoreInteractions(nominatedContactRepository);
    }

    @Test (expected = ApplicationExceptions.EntityAlreadyExistsException.class)
    public void shouldNotCreateNominatedContactWithDuplicateEmailAddress() {

        NominatedContact contact = new NominatedContact(teamUUID, emailAddress);
        Set<NominatedContact> contacts = new HashSet<>();
        contacts.add(contact);

        when(nominatedContactRepository.save(any())).thenThrow(new DataIntegrityViolationException("test"));

        nominatedContactService.createNominatedContact(teamUUID, emailAddress);
    }

    @Test
    public void shouldUpdateNominatedContact() {

        UUID nominatedContactUUID = UUID.randomUUID();
        NominatedContact contact = new NominatedContact(teamUUID, emailAddress);

        when(nominatedContactRepository.findByUuid(nominatedContactUUID)).thenReturn(contact);

        nominatedContactService.updateNominatedContact(nominatedContactUUID, "new@email.com");

        verify(nominatedContactRepository, times(1)).findByUuid(nominatedContactUUID);
        verify(nominatedContactRepository, times(1)).save(any(NominatedContact.class));
        verifyNoMoreInteractions(nominatedContactRepository);

    }

    @Test
    public void shouldDeleteNominatedContact() {
        NominatedContact contact = new NominatedContact(teamUUID, emailAddress);
        Set<NominatedContact> contacts = new HashSet<>();
        contacts.add(new NominatedContact(teamUUID, "one@email.com"));
        contacts.add(contact);

        UUID contactUUID = UUID.randomUUID();

        when(nominatedContactRepository.findByUuid(contactUUID)).thenReturn(contact);
        doNothing().when(nominatedContactRepository).delete(any());

        nominatedContactService.deleteNominatedContact(teamUUID, contactUUID);

        verify(nominatedContactRepository, times(1)).findByUuid(contactUUID);
        verify(nominatedContactRepository, times(1)).delete(contact);
        verifyNoMoreInteractions(nominatedContactRepository);

    }

}
