package uk.gov.digital.ho.hocs.info.nominatedPerson;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.digital.ho.hocs.info.entities.NominatedPerson;
import uk.gov.digital.ho.hocs.info.repositories.NominatedPersonRepository;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class NominatedPeopleServiceTest {

    @Mock
    private NominatedPersonRepository nominatedPersonRepository;

    private NominatedPeopleService nominatedPeopleService;

    private static UUID teamUUID = UUID.randomUUID();
    @Before
    public void setUp() {
        this.nominatedPeopleService = new NominatedPeopleService(nominatedPersonRepository);
    }

    @Test
    public void shouldReturnNominatedPersonSet() {

        when(nominatedPersonRepository.findAllByTeamUUID(teamUUID)).thenReturn(getMockNominatedPeopleForTeam());

        Set<NominatedPerson> nominatedPeople = nominatedPeopleService.getNominatedPerson(teamUUID);

        verify(nominatedPersonRepository, times(1)).findAllByTeamUUID(teamUUID);
        verifyNoMoreInteractions(nominatedPersonRepository);
        assertThat(nominatedPeople.size()).isEqualTo(2);
    }

    private static Set<NominatedPerson> getMockNominatedPeopleForTeam() {
        Set<NominatedPerson> nominatedPeople = new HashSet<>();

        nominatedPeople.add(new NominatedPerson(1, teamUUID, "test@test.com"));
        nominatedPeople.add(new NominatedPerson(2, teamUUID, "test@test.com"));
        return nominatedPeople;
}}