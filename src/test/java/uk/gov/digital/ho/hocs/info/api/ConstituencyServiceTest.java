package uk.gov.digital.ho.hocs.info.api;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.digital.ho.hocs.info.api.dto.CreateConstituencyDto;
import uk.gov.digital.ho.hocs.info.client.auditClient.AuditClient;
import uk.gov.digital.ho.hocs.info.client.caseworkclient.CaseworkClient;
import uk.gov.digital.ho.hocs.info.domain.exception.ApplicationExceptions;
import uk.gov.digital.ho.hocs.info.domain.model.Constituency;
import uk.gov.digital.ho.hocs.info.domain.repository.ConstituencyRepository;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ConstituencyServiceTest {

    @Mock
    private ConstituencyRepository constituencyRepository;

    @Mock
    private CaseworkClient caseworkClient;

    @Mock
    private AuditClient auditClient;

    private ConstituencyService constituencyService;

    private UUID uuid = UUID.randomUUID();

    @Before
    public void setUp() {
        this.constituencyService = new ConstituencyService(constituencyRepository, caseworkClient, auditClient);
    }

    @Test
    public void shouldReturnConstituencyByUUID() {
        when(constituencyRepository.findConstituencyByUUID(uuid)).thenReturn(new Constituency("Constituency1"));

        constituencyService.getConstituency(uuid);

        verify(constituencyRepository, times(1)).findConstituencyByUUID(uuid);
        verifyNoMoreInteractions(constituencyRepository);
    }

    @Test
    public void shouldReturnConstituencyByMemberExternalReference() {
        when(constituencyRepository.findConstituencyByMemberExternalReference("extRef")).thenReturn(new Constituency("Constituency1"));

        constituencyService.getConstituencyByMemberExternalReference("extRef");

        verify(constituencyRepository, times(1)).findConstituencyByMemberExternalReference("extRef");
        verifyNoMoreInteractions(constituencyRepository);
    }

    @Test
    public void shouldReturnAllConstituencys() {
        when(constituencyRepository.findAllBy()).thenReturn(getConstituencys());

        constituencyService.getConstituencys();

        verify(constituencyRepository, times(1)).findAllBy();
        verifyNoMoreInteractions(constituencyRepository);
    }

    @Test
    public void shouldReturnConstituencysForCaseType() {
        when(constituencyRepository.findAllActiveConstituencyByCaseType(any())).thenReturn(getConstituencys());

        constituencyService.getConstituencyList("MIN");

        verify(constituencyRepository, times(1)).findAllActiveConstituencyByCaseType(any());
        verifyNoMoreInteractions(constituencyRepository);
    }

    @Test
    public void shouldCreateConstituencyWithName() {

        CreateConstituencyDto request = new CreateConstituencyDto("Constituency");

        when(constituencyRepository.findConstituencyByName(eq("Constituency"))).thenReturn(null);

        constituencyService.createConstituency(request);

        verify(constituencyRepository, times(1)).findConstituencyByName(eq("Constituency"));
        verify(constituencyRepository, times(1)).save(any());
        verifyNoMoreInteractions(constituencyRepository);
    }

    @Test (expected = ApplicationExceptions.ConstituencyCreationException.class)
    public void shouldNotCreateConstituencyWithExistingName() {

        CreateConstituencyDto request = new CreateConstituencyDto("Constituency");

        when(constituencyRepository.findConstituencyByName(eq("Constituency"))).thenReturn(new Constituency("Constituency"));

        constituencyService.createConstituency(request);
    }


    @Test
    public void shouldDeleteConstituency() {
        when(constituencyRepository.findConstituencyByUUID(any())).thenReturn(new Constituency("Constituency"));

        constituencyService.deleteConstituency(UUID.randomUUID());

        verify(constituencyRepository, times(1)).save(any());
        verify(constituencyRepository, times(1)).findConstituencyByUUID(any());
        verifyNoMoreInteractions(constituencyRepository);
    }

    @Test (expected = ApplicationExceptions.EntityNotFoundException.class)
    public void shouldNotDeleteConstituencyWithInvalidUUID() {
        when(constituencyRepository.findConstituencyByUUID(any())).thenReturn(null);

        constituencyService.deleteConstituency(UUID.randomUUID());
    }

    @Test
    public void shouldDeactivateConstituency(){
        Constituency activeConstituency = new Constituency(1L, uuid, "constituency", null, null, true);

        constituencyService.setConstituencyToInactive(activeConstituency);

        verify(constituencyRepository, times(1)).save(activeConstituency);
        verifyNoMoreInteractions(constituencyRepository);
        verify(auditClient, times(1)).deleteConstituencyAudit(activeConstituency);
        verifyNoMoreInteractions((auditClient));
    }

    @Test
    public void shouldReactivateConstituency(){
        Constituency inactive_constituency = new Constituency(1L, uuid, "constituency", null, null, false);

        when(constituencyRepository.findConstituencyByUUID(any())).thenReturn(inactive_constituency);

        constituencyService.reactivateConstituency(uuid);

        verify(constituencyRepository, times(1)).findConstituencyByUUID(uuid);
        verify(constituencyRepository, times(1)).findConstituencyByUUID(any());
        verify(constituencyRepository, times(1)).save(any());
        verifyNoMoreInteractions(constituencyRepository);
    }

    private List<Constituency> getConstituencys() {
        return new ArrayList<Constituency>() {{
            add(new Constituency("Constituency1"));
            add(new Constituency("Constituency2"));
        }};

    }
}
