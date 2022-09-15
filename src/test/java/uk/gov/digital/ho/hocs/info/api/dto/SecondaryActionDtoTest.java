package uk.gov.digital.ho.hocs.info.api.dto;

import org.junit.Test;
import uk.gov.digital.ho.hocs.info.domain.model.SecondaryAction;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class SecondaryActionDtoTest {

    @Test
    public void from() {

        Long id = 11l;
        UUID uuid = UUID.randomUUID();
        String component = "Component";
        String name = "Name";
        String label = "Label";
        String validation = "Validation";
        String props = "props";

        SecondaryAction secondaryAction = new SecondaryAction(id, uuid, component, name, label, validation, props);

        SecondaryActionDto result = SecondaryActionDto.from(secondaryAction);

        assertThat(result.getUuid()).isEqualTo(uuid);
        assertThat(result.getComponent()).isEqualTo(component);
        assertThat(result.getName()).isEqualTo(name);
        assertThat(result.getLabel()).isEqualTo(label);
        assertThat(result.getValidation()).isEqualTo(validation);
        assertThat(result.getProps()).isEqualTo(props);

    }

}
