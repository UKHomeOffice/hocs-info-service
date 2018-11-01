package uk.gov.digital.ho.hocs.info.dto;

import org.junit.Test;
import uk.gov.digital.ho.hocs.info.entities.Minister;
import uk.gov.digital.ho.hocs.info.entities.Template;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

public class TemplateDtoTest {

    UUID uuid = UUID.randomUUID();

    @Test
    public void from() {
        Template template = new Template(1l,"DisplayName", "MIN", uuid,Boolean.FALSE);

        TemplateDto templateDto = TemplateDto.from(template);

        assertThat(templateDto.getDisplayName()).isEqualTo("DisplayName");
        assertThat(templateDto.getUuid()).isEqualTo(uuid);

    }
}