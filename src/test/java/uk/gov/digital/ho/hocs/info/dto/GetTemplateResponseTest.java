package uk.gov.digital.ho.hocs.info.dto;

import org.junit.Test;
import uk.gov.digital.ho.hocs.info.entities.Template;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

public class GetTemplateResponseTest {

    UUID uuid1 = UUID.randomUUID();
    UUID uuid2 = UUID.randomUUID();


    @Test
    public void from() {
        List<Template> templates = new ArrayList<>();
        templates.add(new Template("Template1", "MIN", uuid1));
        templates.add(new Template("Template2", "MIN", uuid2));

        GetTemplateResponse getTemplateResponse = GetTemplateResponse.from(templates);

        List<TemplateDto> responseAsList = new ArrayList<>(Objects.requireNonNull(getTemplateResponse.getTemplateDtos()));

        TemplateDto result1 = responseAsList.stream().filter(x -> "Template1".equals(x.getDisplayName())).findAny().orElse(null);
        assertThat(result1).isNotNull();
        assertThat(result1.getUuid()).isEqualTo(uuid1);
        TemplateDto result2 = responseAsList.stream().filter(x -> "Template2".equals(x.getDisplayName())).findAny().orElse(null);
        assertThat(result2).isNotNull();
        assertThat(result2.getUuid()).isEqualTo(uuid2);

    }
}