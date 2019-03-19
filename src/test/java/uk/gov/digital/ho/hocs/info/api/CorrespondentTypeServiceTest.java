package uk.gov.digital.ho.hocs.info.api;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.digital.ho.hocs.info.api.CorrespondentTypeService;
import uk.gov.digital.ho.hocs.info.client.auditClient.AuditClient;
import uk.gov.digital.ho.hocs.info.domain.model.CorrespondentType;
import uk.gov.digital.ho.hocs.info.domain.repository.CorrespondentTypeRepository;

import java.util.HashSet;
import java.util.UUID;

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
    public void shouldCreateCorrespondentType() {

        CorrespondentType response = correspondentTypeService.createCorrespondentType("name","NAME");

        assertThat(response).isNotNull();
        verify(correspondentTypeRepository, times(1)).save(any());
        verifyNoMoreInteractions(correspondentTypeRepository);

    }
}