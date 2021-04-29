package uk.gov.digital.ho.hocs.info.api.dto;

import org.junit.Test;
import uk.gov.digital.ho.hocs.info.domain.model.ValidationRule;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class ValidationDtoTest {

    @Test
    public void from() {
        UUID uuid = UUID.randomUUID();
        ValidationRule validationRule = new ValidationRule(1L, uuid, "title", "{}");

        ValidationDto validationDto = ValidationDto.from(validationRule);

        assertThat(validationDto.getUuid()).isEqualTo(uuid);
        assertThat(validationDto.getTitle()).isEqualTo("title");
        assertThat(validationDto.getSubSchema()).isEqualTo("{}");
    }
}
