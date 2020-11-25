package uk.gov.digital.ho.hocs.info.api;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;
import uk.gov.digital.ho.hocs.info.api.dto.SomuTypeDto;
import uk.gov.digital.ho.hocs.info.domain.model.SomuType;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SomuTypeResourceTest {

    @Mock
    SomuTypeService somuTypeService;

    SomuTypeResource somuTypeResource;

    @Before
    public void setup() {
        somuTypeResource = new SomuTypeResource(somuTypeService);
    }

    @Test
    public void getAllSomuTypes() {
        SomuType somuType = new SomuType(1L, UUID.randomUUID(), "CaseType", "Type", "{}", true);
        Set<SomuType> somuTypes = new HashSet<SomuType>() {{
            add(somuType);
        }};
        when(somuTypeService.getAllSomuTypes()).thenReturn(somuTypes);

        ResponseEntity<Set<SomuTypeDto>> result = somuTypeResource.getAllSomuTypes();

        assertThat(result.getBody().size()).isEqualTo(1);
        SomuTypeDto somuTypeDto = (SomuTypeDto) result.getBody().toArray()[0];
        assertThat(somuTypeDto.getCaseType()).isEqualTo("CaseType");
        assertThat(somuTypeDto.getType()).isEqualTo("Type");
        assertThat(somuTypeDto.getSchema()).isEqualTo("{}");
        assertThat(somuTypeDto.isActive()).isTrue();
    }

    @Test
    public void getSomuTypeForCaseTypeAndType() {
        SomuType somuType = new SomuType(1L, UUID.randomUUID(), "CaseType", "Type", "{}", true);
        when(somuTypeService.getSomuTypeForCaseTypeAndType("CaseType", "Type")).thenReturn(somuType);

        ResponseEntity<SomuTypeDto> result = somuTypeResource.getSomuTypeForCaseTypeAndType("CaseType", "Type");

        SomuTypeDto somuTypeDto = result.getBody();
        assertThat(somuTypeDto.getCaseType()).isEqualTo("CaseType");
        assertThat(somuTypeDto.getType()).isEqualTo("Type");
        assertThat(somuTypeDto.getSchema()).isEqualTo("{}");
        assertThat(somuTypeDto.isActive()).isTrue();
    }
}
