package uk.gov.digital.ho.hocs.info.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

}
