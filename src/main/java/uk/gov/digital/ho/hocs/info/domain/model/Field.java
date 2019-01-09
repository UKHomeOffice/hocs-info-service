package uk.gov.digital.ho.hocs.info.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;
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
    @Column(name = "data")
    private String data;

    @Getter
    @Column(name = "summary")
    private boolean summary;

    @Getter
    @Column(name = "active")
    private boolean active = true;

    @Setter
    @Getter
    @ManyToOne
    @JoinColumn(name = "form_uuid", referencedColumnName = "uuid")
    private Form form;

    public Field(String data, boolean summary) {
        this.uuid = UUID.randomUUID();
        this.data = data;
        this.summary = summary;
    }

}
