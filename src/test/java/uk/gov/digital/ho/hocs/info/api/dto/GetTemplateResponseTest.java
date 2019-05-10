package uk.gov.digital.ho.hocs.info.api.dto;

import org.junit.Test;
import uk.gov.digital.ho.hocs.info.domain.model.Template;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class GetTemplateResponseTest {

    UUID uuid = UUID.randomUUID();
    UUID documentUUID = UUID.randomUUID();

    @Test
    public void from() {
        Template template = new Template(1l, uuid, documentUUID, "DisplayName", "MIN", Boolean.FALSE);

        TemplateDto templateDto = TemplateDto.from(template);

        assertThat(templateDto.getDisplayName()).isEqualTo("DisplayName");
        assertThat(templateDto.getUuid()).isEqualTo(uuid);
        assertThat(templateDto.getDocumentUUID()).isEqualTo(documentUUID);
    }
}