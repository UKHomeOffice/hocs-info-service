package uk.gov.digital.ho.hocs.info.domain.model;

import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class SomuTypeTest {

    @Test
    public void shouldGenerateUuid() {
        SomuType somuType = new SomuType(
                "caseType",
                "type",
                "schema",
                true);

        assertThat(somuType.getCaseType()).isEqualTo("caseType");
        assertThat(somuType.getType()).isEqualTo("type");
    }

    @Test
    public void deleteSetsActiveFlagToFalse() {
        SomuType somuType = new SomuType(
                "caseType",
                "type",
                "schema",
                true);

        somuType.delete();

        assertThat(somuType.isActive()).isFalse();
    }
}
