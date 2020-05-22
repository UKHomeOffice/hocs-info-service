package uk.gov.digital.ho.hocs.info.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Entity
@IdClass(FieldScreen.class)
@Table(name = "field_screen")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class FieldScreen implements Serializable {

    @Id
    @Column(name = "schema_uuid")
    private UUID schemaUUID;

    @Id
    @Column(name = "field_uuid")
    private UUID fieldUUID;

    @Column(name = "sort_order")
    private Long sortOrder;

    @ManyToOne()
    @JoinColumn(name = "field_uuid", referencedColumnName = "uuid", insertable = false, updatable = false)
    private Field field;


}
