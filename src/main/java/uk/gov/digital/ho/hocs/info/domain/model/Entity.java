package uk.gov.digital.ho.hocs.info.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import uk.gov.digital.ho.hocs.info.api.dto.EntityDto;

import javax.persistence.*;
import java.util.UUID;


@NoArgsConstructor
@AllArgsConstructor
@javax.persistence.Entity
@Table(name = "entity")
public class Entity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Column(name = "uuid")
    private UUID uuid;

    @Getter
    @Column(name = "simple_name")
    private String simpleName;

    @Getter
    @Column(name = "data")
    private String data = "{}";

    @Getter
    @Column(name = "entity_list_uuid")
    private UUID entityListUUID;

    @Getter
    @Column(name = "active")
    private boolean active;

    @Getter
    @Column(name = "sort_order")
    private int sortOrder;

    public Entity(UUID uuid, String simpleName, String data, UUID entityListUUID, boolean active, int sortOrder) {
        this.uuid = uuid;
        this.simpleName = simpleName;
        this.data = data;
        this.entityListUUID = entityListUUID;
        this.active = active;
        this.sortOrder = sortOrder;
    }

    public void update(EntityDto entityDto) {
        /*
          We only update the title in the data here, but retain the simpleName value.
          Reason is the value of simple name is stored in the case data blob and we have to
          retain it to support old cases. Simple names are used in the display.

          @TODO Investigate switching how entity list management is performed and potential to use UUIDs instead
         */
        this.data = entityDto.getData();
    }
}
