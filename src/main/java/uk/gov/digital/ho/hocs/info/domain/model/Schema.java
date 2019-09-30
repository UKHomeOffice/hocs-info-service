package uk.gov.digital.ho.hocs.info.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

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
    @Column(name = "stage_type", insertable = false, updatable = false)
    private String stageType;

    @Getter
    @OneToMany
    @JoinTable(
            name="field_screen",
            joinColumns = @JoinColumn(name="schema_uuid", referencedColumnName="uuid"),
            inverseJoinColumns = @JoinColumn( name="field_uuid", referencedColumnName="uuid")
    )
    @OrderBy("id")
    private List<Field> fields;

    public Schema(String type, String title, String actionLabel) {
        this.uuid = UUID.randomUUID();
        this.type = type;
        this.title = title;
        this.actionLabel = actionLabel;
        this.fields = new ArrayList<>(0);
    }

    public void addField(Field field) {
        fields.add(field);
    }
    public void removeField(UUID fieldUUID) {
        fields.removeIf(field -> field.getUuid() == fieldUUID);

    }

}
