package uk.gov.digital.ho.hocs.info.api;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.digital.ho.hocs.info.client.auditClient.AuditClient;
import uk.gov.digital.ho.hocs.info.domain.exception.ApplicationExceptions;
import uk.gov.digital.ho.hocs.info.domain.model.CorrespondentType;
import uk.gov.digital.ho.hocs.info.domain.repository.CorrespondentTypeRepository;

import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class CorrespondentTypeServiceTest {

    @Mock
    private CorrespondentTypeRepository correspondentTypeRepository;

    @Mock
    private AuditClient auditClient;

    private CorrespondentTypeService correspondentTypeService;


    @Before
    public void setUp() {
        this.correspondentTypeService = new CorrespondentTypeService(correspondentTypeRepository, auditClient);
    }

    @Test
    public void shouldReturnAllCorrespondentTypes() {
        when(correspondentTypeRepository.findAll()).thenReturn(new HashSet<>());

        correspondentTypeService.getAllCorrespondentTypes();
        verify(correspondentTypeRepository, times(1)).findAll();
        verifyNoMoreInteractions(correspondentTypeRepository);

    }

    @Test
    public void shouldGetCorrespondentTypesByCaseType() {
        when(correspondentTypeRepository.findAllByCaseType("CASE_TYPE")).thenReturn(new HashSet<>());

        correspondentTypeService.getCorrespondentTypesByCaseType("CASE_TYPE");

        verify(correspondentTypeRepository).findAllByCaseType("CASE_TYPE");
        verifyNoMoreInteractions(correspondentTypeRepository);
    }

    @Test
    public void shouldCreateCorrespondentType() {

        CorrespondentType correspondentType = correspondentTypeService.createCorrespondentType("name","NAME");

        assertThat(correspondentType).isNotNull();
        verify(correspondentTypeRepository, times(1)).save(any());
        verifyNoMoreInteractions(correspondentTypeRepository);

    }

    @Test(expected = ApplicationExceptions.EntityCreationException.class)
    public void shouldThrowExceptionWhenCreatingCorrespondentTypeWithNoDisplayName() {

        correspondentTypeService.createCorrespondentType(null,"NAME");

    }

    @Test
    public void shouldNotCreateCorrespondentTypeWithNoDisplayName() {

        try { correspondentTypeService.createCorrespondentType( "Name", null);
        } catch (ApplicationExceptions.EntityCreationException e) {
            // Do nothing.
        }
        verifyZeroInteractions(correspondentTypeRepository);
    }

    @Test(expected = ApplicationExceptions.EntityCreationException.class)
    public void shouldThrowExceptionWhenCreatingCorrespondentTypeWithNoType() {

        correspondentTypeService.createCorrespondentType(null,"NAME");

    }

    @Test
    public void shouldNotCreateCorrespondentTypeWithNoType() {

        try { correspondentTypeService.createCorrespondentType("Name", null);
        } catch (ApplicationExceptions.EntityCreationException e) {
            // Do nothing.
        }
        verifyZeroInteractions(correspondentTypeRepository);
    }

}