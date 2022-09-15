package uk.gov.digital.ho.hocs.info.api.dto;

import org.junit.Test;
import uk.gov.digital.ho.hocs.info.domain.model.SomuType;

import static org.assertj.core.api.Assertions.assertThat;

public class SomuTypeDtoTest {

    @Test
    public void from() {
        SomuType somuType = new SomuType("caseType", "type", "schema", true);

        SomuTypeDto somuTypeDto = SomuTypeDto.from(somuType);

        assertThat(somuTypeDto).isNotNull();
        assertThat(somuTypeDto.getUuid()).isNotNull();
        assertThat(somuTypeDto.getCaseType()).isEqualTo("caseType");
        assertThat(somuTypeDto.getType()).isEqualTo("type");
        assertThat(somuTypeDto.getSchema()).isEqualTo("schema");
        assertThat(somuTypeDto.isActive()).isTrue();
    }

}
