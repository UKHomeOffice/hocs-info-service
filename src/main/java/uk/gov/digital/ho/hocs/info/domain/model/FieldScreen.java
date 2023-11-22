package uk.gov.digital.ho.hocs.info.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.UUID;

@jakarta.persistence.Entity
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
