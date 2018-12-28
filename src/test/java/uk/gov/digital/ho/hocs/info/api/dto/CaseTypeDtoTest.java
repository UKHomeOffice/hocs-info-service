package uk.gov.digital.ho.hocs.info.api.dto;

import org.junit.Test;
import uk.gov.digital.ho.hocs.info.domain.model.CaseTypeEntity;

import static org.assertj.core.api.Assertions.assertThat;

public class CaseTypeDtoTest {

    @Test
    public void shouldBuildCaseTypeDtoFromCaseTypeObject() {
        CaseTypeEntity caseTypeEntity = new CaseTypeEntity(1L,"Name","a1", "MIN","DCU", "DCU_MIN_DISPATCH", true);

        CaseTypeDto caseTypeDto = CaseTypeDto.from(caseTypeEntity);

        assertThat(caseTypeDto.getDisplayName()).isEqualTo("Name");
        assertThat(caseTypeDto.getType()).isEqualTo("MIN");

    }
}