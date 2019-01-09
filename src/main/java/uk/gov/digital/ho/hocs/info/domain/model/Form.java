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
@Table(name = "form")
@NoArgsConstructor
@AllArgsConstructor
public class Form implements Serializable {

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
    @Column(name = "data")
    private String data;

    @Getter
    @Column(name = "active")
    private boolean active;

    @Getter
    @OneToMany(mappedBy = "form", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private Set<Field> fields;

    public Form(String type, String data) {
        this.uuid = UUID.randomUUID();
        this.type = type;
        this.data = data;
        this.fields = new HashSet<>(0);
    }

    public void addField(Field field) {
        field.setForm(this);
        fields.add(field);
    }
    public void removeField(UUID fieldUUID) {
        fields.removeIf(field -> field.getUuid() == fieldUUID);

    }

}
