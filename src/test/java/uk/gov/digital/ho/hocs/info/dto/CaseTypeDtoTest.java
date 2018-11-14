package uk.gov.digital.ho.hocs.info.dto;

import org.junit.Test;
import uk.gov.digital.ho.hocs.info.entities.CaseTypeEntity;
import uk.gov.digital.ho.hocs.info.entities.CorrespondentType;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

public class CaseTypeDtoTest {

    @Test
    public void shouldBuildCaseTypeDtoFromCaseTypeObject() {
        CaseTypeEntity caseTypeEntity = new CaseTypeEntity(1L,"Name","MIN","DCU");

        CaseTypeDto caseTypeDto = CaseTypeDto.from(caseTypeEntity);

        assertThat(caseTypeDto.getDisplayName()).isEqualTo("Name");
        assertThat(caseTypeDto.getType()).isEqualTo("MIN");

    }
}