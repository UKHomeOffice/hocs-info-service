package uk.gov.digital.ho.hocs.info.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@jakarta.persistence.Entity
@Table(name = "entity_list")
public class EntityList {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Column(name = "uuid", unique = true)
    private UUID uuid;

    @Getter
    @Column(name = "display_name")
    private String displayName;

    @Column(name = "simple_name", unique = true)
    @Getter
    private String simpleName;

    @Getter
    @Column(name = "active")
    private boolean active;

    public EntityList(UUID uuid, String displayName, String simpleName, boolean active) {
        this.uuid = uuid;
        this.displayName = displayName;
        this.simpleName = simpleName;
        this.active = active;
    }

}
