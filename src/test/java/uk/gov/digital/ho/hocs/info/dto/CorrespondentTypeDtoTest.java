package uk.gov.digital.ho.hocs.info.dto;

import org.junit.Test;
import uk.gov.digital.ho.hocs.info.entities.CorrespondentType;

import static org.assertj.core.api.Assertions.*;

public class CorrespondentTypeDtoTest {

    @Test
    public void shouldBuildCorrespondentTypeDTOFromCorrespondentObject() {
        CorrespondentType correspondentType = new CorrespondentType(1,"Name","MIN");

        CorrespondentTypeDto correspondentTypeDto = CorrespondentTypeDto.from(correspondentType);

        assertThat(correspondentTypeDto.getDisplayName()).isEqualTo("Name");
        assertThat(correspondentTypeDto.getType()).isEqualTo("MIN");
    }
}