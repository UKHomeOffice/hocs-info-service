package uk.gov.digital.ho.hocs.info.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.UUID;

@javax.persistence.Entity
@IdClass(FieldScreen.class)
@Table(name = "field_screen")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Deprecated(forRemoval = true)
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
