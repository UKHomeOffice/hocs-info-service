package uk.gov.digital.ho.hocs.info.domain.model;

import org.junit.Before;
import org.junit.Test;
import uk.gov.digital.ho.hocs.info.api.dto.EntityDto;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class EntityTest {

    private Long id = 1L;

    private String simpleName = "name123";

    private String data = "{ title: 'Title 321' }";

    private UUID uuid = UUID.randomUUID();

    private UUID listUuid = UUID.randomUUID();

    private boolean active = true;

    private Entity entity;

    @Before
    public void before() {
        this.entity = new Entity(id, uuid, simpleName, data, listUuid, active, 10);
    }

    @Test
    public void updateShouldOnlyAmendData() {
        String newSimpleName = "name";
        String newUuid = UUID.randomUUID().toString();
        String newData = "data";
        EntityDto entityDto = new EntityDto(newSimpleName, newUuid, newData);

        entity.update(entityDto);

        assertThat(entity.getUuid()).isEqualTo(uuid);
        assertThat(entity.getSimpleName()).isEqualTo(simpleName);
        assertThat(entity.getEntityListUUID()).isEqualTo(listUuid);
        assertThat(entity.isActive()).isEqualTo(active);
        assertThat(entity.getData()).isEqualTo(newData);
    }

}
