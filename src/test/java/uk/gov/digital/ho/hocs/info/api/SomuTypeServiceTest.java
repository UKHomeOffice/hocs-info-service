package uk.gov.digital.ho.hocs.info.api;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.digital.ho.hocs.info.domain.model.SomuType;
import uk.gov.digital.ho.hocs.info.domain.repository.SomuTypeRepository;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SomuTypeServiceTest {

    @Mock
    private SomuTypeRepository somuTypeRepository;

    private SomuTypeService service;

    @Before
    public void setup() {
        service = new SomuTypeService(somuTypeRepository);
    }

    @Test
    public void getAllSomuTypes() {
        SomuType somuType = new SomuType(1L, UUID.randomUUID(), "CaseType", "Type", "{}", true);
        Set<SomuType> somuTypes = new HashSet<SomuType>() {{
            add(somuType);
        }};
        when(somuTypeRepository.findAll()).thenReturn(somuTypes);

        Set<SomuType> result = service.getAllSomuTypes();

        assertThat(result.size()).isEqualTo(1);
        assertThat(result).isEqualTo(somuTypes);
        assertThat(result.toArray()[0]).isEqualTo(somuType);
    }

    @Test
    public void getAllSomuTypesForCaseType() {
        // Given
        SomuType somuType = new SomuType(1L, UUID.randomUUID(), "CaseType", "Type", "{}", true);
        Set<SomuType> somuTypes = new HashSet<SomuType>() {{
            add(somuType);
        }};

        // When
        when(somuTypeRepository.findAllByCaseType("CaseType")).thenReturn(somuTypes);

        Set<SomuType> result = service.getAllSomuTypesForCaseType("CaseType");

        // Then

        assertThat(result.size()).isEqualTo(1);
        assertThat(result).isEqualTo(somuTypes);
        assertThat(result.toArray()[0]).isEqualTo(somuType);
    }

    @Test
    public void getSomuTypeForCaseTypeAndType() {
        SomuType somuType = new SomuType(1L, UUID.randomUUID(), "CaseType", "Type", "{}", true);
        when(somuTypeRepository.findByCaseTypeAndType("CaseType", "Type")).thenReturn(somuType);

        SomuType result = service.getSomuTypeForCaseTypeAndType("CaseType", "Type");

        assertThat(result).isEqualTo(somuType);
    }

    @Test
    public void upsertSomuTypeForCaseTypeAndTypeWhenInsert() {
        when(somuTypeRepository.findByCaseTypeAndType("CaseType", "Type")).thenReturn(null);

        SomuType result = service.upsertSomuTypeForCaseTypeAndType("CaseType", "Type", "{}", true);

        assertThat(result).isNotNull();
        assertThat(result.getUuid()).isNotNull();
        assertThat(result.getCaseType()).isEqualTo("CaseType");
        assertThat(result.getType()).isEqualTo("Type");
        assertThat(result.getSchema()).isEqualTo("{}");
        assertThat(result.isActive()).isTrue();
        verify(somuTypeRepository).findByCaseTypeAndType("CaseType", "Type");
        verify(somuTypeRepository).save(any(SomuType.class));
        verifyNoMoreInteractions(somuTypeRepository);
    }

    @Test
    public void upsertSomuTypeForCaseTypeAndTypeWhenUpdate() {
        SomuType somuType = new SomuType(1L, UUID.randomUUID(), "CaseType", "Type", "{}", true);
        when(somuTypeRepository.findByCaseTypeAndType("CaseType", "Type")).thenReturn(somuType);

        SomuType result = service.upsertSomuTypeForCaseTypeAndType("CaseType", "Type", "{'test':true}", true);

        assertThat(result).isNotNull();
        assertThat(result.getUuid()).isNotNull();
        assertThat(result.getCaseType()).isEqualTo("CaseType");
        assertThat(result.getType()).isEqualTo("Type");
        assertThat(result.getSchema()).isEqualTo("{'test':true}");
        assertThat(result.isActive()).isTrue();
        verify(somuTypeRepository).findByCaseTypeAndType("CaseType", "Type");
        verify(somuTypeRepository).save(somuType);
        verifyNoMoreInteractions(somuTypeRepository);
    }
}
