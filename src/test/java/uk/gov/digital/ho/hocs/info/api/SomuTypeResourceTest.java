package uk.gov.digital.ho.hocs.info.api;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;
import uk.gov.digital.ho.hocs.info.api.dto.CreateSomuTypeDto;
import uk.gov.digital.ho.hocs.info.api.dto.SomuTypeDto;
import uk.gov.digital.ho.hocs.info.domain.model.SomuType;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SomuTypeResourceTest {

    private UUID somuUuid = UUID.randomUUID();

    @Mock
    SomuTypeService somuTypeService;

    SomuTypeResource somuTypeResource;

    @Before
    public void setup() {
        somuTypeResource = new SomuTypeResource(somuTypeService);
    }

    @Test
    public void getAllSomuTypes() {
        SomuType somuType = new SomuType(1L, somuUuid, "CaseType", "Type", "{}", true);
        Set<SomuType> somuTypes = new HashSet<SomuType>() {{
            add(somuType);
        }};
        when(somuTypeService.getAllSomuTypes()).thenReturn(somuTypes);

        ResponseEntity<Set<SomuTypeDto>> result = somuTypeResource.getAllSomuTypes();

        assertThat(result).isNotNull();
        assertThat(result.getStatusCodeValue()).isEqualTo(200);
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().size()).isEqualTo(1);
        SomuTypeDto somuTypeDto = (SomuTypeDto) result.getBody().toArray()[0];
        assertThat(somuTypeDto.getUuid()).isEqualTo(somuUuid);
        assertThat(somuTypeDto.getCaseType()).isEqualTo("CaseType");
        assertThat(somuTypeDto.getType()).isEqualTo("Type");
        assertThat(somuTypeDto.getSchema()).isEqualTo("{}");
        assertThat(somuTypeDto.isActive()).isTrue();
        verify(somuTypeService).getAllSomuTypes();
        verifyNoMoreInteractions(somuTypeService);
    }

    @Test
    public void getAllSomuTypesForCaseType() {
        // Given
        SomuType somuType = new SomuType(1L, somuUuid, "CaseType", "Type", "{}", true);
        Set<SomuType> somuTypes = new HashSet<SomuType>() {{
            add(somuType);
        }};

        when(somuTypeService.getAllSomuTypesForCaseType("CaseType")).thenReturn(somuTypes);

        // When
        final Set<SomuType> result = somuTypeService.getAllSomuTypesForCaseType("CaseType");

        // Then
        assertThat(result.size()).isEqualTo(1);
        assertThat(result).isEqualTo(somuTypes);

    }

    @Test
    public void getSomuTypeForCaseTypeAndType() {
        SomuType somuType = new SomuType(1L, somuUuid, "CaseType", "Type", "{}", true);
        when(somuTypeService.getSomuTypeForCaseTypeAndType("CaseType", "Type")).thenReturn(somuType);

        ResponseEntity<SomuTypeDto> result = somuTypeResource.getSomuTypeForCaseTypeAndType("CaseType", "Type");

        assertThat(result).isNotNull();
        assertThat(result.getStatusCodeValue()).isEqualTo(200);
        assertThat(result.getBody()).isNotNull();
        SomuTypeDto somuTypeDto = result.getBody();
        assertThat(somuTypeDto.getUuid()).isEqualTo(somuUuid);
        assertThat(somuTypeDto.getCaseType()).isEqualTo("CaseType");
        assertThat(somuTypeDto.getType()).isEqualTo("Type");
        assertThat(somuTypeDto.getSchema()).isEqualTo("{}");
        assertThat(somuTypeDto.isActive()).isTrue();
        verify(somuTypeService).getSomuTypeForCaseTypeAndType("CaseType", "Type");
        verifyNoMoreInteractions(somuTypeService);
    }

    @Test
    public void upsertSomuTypeForCaseTypeAndType() {
        SomuType somuType = new SomuType(1L, somuUuid, "CaseType", "Type", "{}", true);
        when(somuTypeService.upsertSomuTypeForCaseTypeAndType("CaseType", "Type", "{}", true)).thenReturn(somuType);
        CreateSomuTypeDto createSomuTypeDto = new CreateSomuTypeDto("CaseType", "Type", "{}", true);

        ResponseEntity<SomuTypeDto> result = somuTypeResource.upsertSomuTypeForCaseTypeAndType(createSomuTypeDto);

        assertThat(result).isNotNull();
        assertThat(result.getStatusCodeValue()).isEqualTo(200);
        assertThat(result.getBody()).isNotNull();
        SomuTypeDto somuTypeDto = result.getBody();
        assertThat(somuTypeDto.getUuid()).isEqualTo(somuUuid);
        assertThat(somuTypeDto.getCaseType()).isEqualTo("CaseType");
        assertThat(somuTypeDto.getType()).isEqualTo("Type");
        assertThat(somuTypeDto.getSchema()).isEqualTo("{}");
        assertThat(somuTypeDto.isActive()).isTrue();
        verify(somuTypeService).upsertSomuTypeForCaseTypeAndType("CaseType", "Type", "{}", true);
        verifyNoMoreInteractions(somuTypeService);
    }

}
