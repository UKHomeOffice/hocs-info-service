package uk.gov.digital.ho.hocs.info.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "somu_type")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class SomuType {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "uuid")
    private UUID uuid;

    @Column(name = "case_type")
    private String caseType;

    @Column(name = "type")
    private String type;

    @Column(name = "schema")
    private String schema = "{}";

    @Column(name = "active")
    private boolean active;

    public SomuType(String caseType, String type, String schema, boolean active) {
        this.uuid = UUID.randomUUID();
        this.caseType = caseType;
        this.type = type;
        this.schema = schema;
        this.active = active;
    }
}
