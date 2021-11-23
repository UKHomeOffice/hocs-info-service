package uk.gov.digital.ho.hocs.info.api.dto;

import org.junit.Test;
import uk.gov.digital.ho.hocs.info.domain.model.CorrespondentType;

import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

public class CorrespondentTypeDtoTest {

    @Test
    public void shouldBuildCorrespondentTypeDTOFromCorrespondentObject() {
        CorrespondentType correspondentType = new CorrespondentType(1L, UUID.randomUUID(),"Name","MIN");

        CorrespondentTypeDto correspondentTypeDto = CorrespondentTypeDto.from(correspondentType);

        assertThat(correspondentTypeDto.getDisplayName()).isEqualTo("Name");
        assertThat(correspondentTypeDto.getType()).isEqualTo("MIN");
    }
}