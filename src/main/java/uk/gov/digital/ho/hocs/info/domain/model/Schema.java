package uk.gov.digital.ho.hocs.info.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "screen_schema")
@NoArgsConstructor
@AllArgsConstructor
public class Schema implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Column(name = "uuid")
    private UUID uuid;

    @Getter
    @Column(name = "type")
    private String type;

    @Getter
    @Column(name = "title")
    private String title;

    @Getter
    @Column(name = "action_label")
    private String actionLabel;

    @Getter
    @Column(name = "active")
    private boolean active;

    @Getter
    @OneToMany(mappedBy = "schema", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private Set<Field> fields;

    public Schema(String type, String title, String actionLabel) {
        this.uuid = UUID.randomUUID();
        this.type = type;
        this.title = title;
        this.actionLabel = actionLabel;
        this.fields = new HashSet<>(0);
    }

    public void addField(Field field) {
        field.setSchema(this);
        fields.add(field);
    }
    public void removeField(UUID fieldUUID) {
        fields.removeIf(field -> field.getUuid() == fieldUUID);

    }

}
