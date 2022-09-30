package uk.gov.digital.ho.hocs.info.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.UUID;

@javax.persistence.Entity
@Table(name = "field")
@NoArgsConstructor
@AllArgsConstructor
public class Field implements Serializable {

    @Id
    @Getter
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
    @Column(name = "props")
    private String props;

    @Getter
    @Column(name = "summary")
    private boolean summary;

    @Getter
    @Column(name = "active")
    private boolean active = true;

    @Getter
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "child_field", referencedColumnName = "uuid")
    private Field child = null;

    public Field(String component,
                 String name,
                 String label,
                 String validation,
                 String props,
                 boolean summary,
                 Field child) {
        this.uuid = UUID.randomUUID();
        this.component = component;
        this.name = name;
        this.label = label;
        this.validation = validation;
        this.props = props;
        this.summary = summary;
        this.child = child;
    }

}
