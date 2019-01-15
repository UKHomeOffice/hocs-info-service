package uk.gov.digital.ho.hocs.info.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "field")
@NoArgsConstructor
@AllArgsConstructor
public class Field implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Column(name = "uuid")
    private UUID uuid;

    @Getter
    @Column(name = "component")
    private String component;

    @Getter
    @Column(name = "name")
    private String name;

    @Getter
    @Column(name = "label")
    private String label;

    @Getter
    @Column(name = "validation")
    private String validation;

    @Getter
    @Column(name = "choices")
    private String choices;

    @Getter
    @Column(name = "summary")
    private boolean summary;

    @Getter
    @Column(name = "active")
    private boolean active = true;

    @Setter
    @Getter
    @ManyToOne
    @JoinColumn(name = "schema_uuid", referencedColumnName = "uuid")
    private Schema schema;

    public Field(UUID schemaUUID, String component, String name, String label, String validation, String choices, boolean summary) {
        this.uuid = UUID.randomUUID();
        this.component = component;
        this.name = name;
        this.label = label;
        this.validation = validation;
        this.choices = choices;
        this.summary = summary;
    }

}
