package uk.gov.digital.ho.hocs.info.api;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.digital.ho.hocs.info.api.NominatedContactService;
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

    private uk.gov.digital.ho.hocs.info.api.NominatedContactService NominatedContactService;

    private static UUID teamUUID = UUID.randomUUID();
    @Before
    public void setUp() {
        this.NominatedContactService = new NominatedContactService(nominatedContactRepository);
    }

    @Test
    public void shouldReturnNominatedContactSet() {

        when(nominatedContactRepository.findAllByTeamUUID(teamUUID)).thenReturn(getMockNominatedContactForTeam());

        Set<NominatedContact> NominatedContact = NominatedContactService.getNominatedContact(teamUUID);

        verify(nominatedContactRepository, times(1)).findAllByTeamUUID(teamUUID);
        verifyNoMoreInteractions(nominatedContactRepository);
        assertThat(NominatedContact.size()).isEqualTo(2);
    }

    private static Set<NominatedContact> getMockNominatedContactForTeam() {
        Set<NominatedContact> NominatedContact = new HashSet<>();

        NominatedContact.add(new NominatedContact(1l, teamUUID, "test@test.com"));
        NominatedContact.add(new NominatedContact(2l, teamUUID, "test@test.com"));
        return NominatedContact;
}}