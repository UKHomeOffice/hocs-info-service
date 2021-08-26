package uk.gov.digital.ho.hocs.info.domain.entity.dto;

import org.junit.Test;
import uk.gov.digital.ho.hocs.info.domain.entity.Entity;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class EntityDtoTest {

    @Test
    public void from() {
        String simpleName = "name123";
        UUID uuid = UUID.randomUUID();
        String data = "{ title: 'Title 321' }";
        Entity entity = new Entity(1L, uuid, simpleName, data, UUID.randomUUID(), true, 10);

        EntityDto entityDto = EntityDto.from(entity);

        assertThat(entityDto.getSimpleName()).isEqualTo(simpleName);
        assertThat(entityDto.getData()).isEqualTo(data);
        assertThat(entityDto.getUuid()).isEqualTo(uuid.toString());
    }
}
