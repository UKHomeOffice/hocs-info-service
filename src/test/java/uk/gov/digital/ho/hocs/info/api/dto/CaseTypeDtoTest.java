package uk.gov.digital.ho.hocs.info.api.dto;

import org.junit.Test;
import uk.gov.digital.ho.hocs.info.domain.model.CaseType;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class CaseTypeDtoTest {

    @Test
    public void shouldBuildCaseTypeDtoFromCaseTypeObject() {
        CaseType caseType = new CaseType(1L, UUID.randomUUID(),"Name","a1", "MIN",UUID.randomUUID(), "DCU_MIN_DISPATCH", true, true, null);

        CaseTypeDto caseTypeDto = CaseTypeDto.from(caseType);

        assertThat(caseTypeDto.getDisplayName()).isEqualTo("Name");
        assertThat(caseTypeDto.getType()).isEqualTo("MIN");

    }
}