package uk.gov.digital.ho.hocs.info.domain.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@jakarta.persistence.Entity
@Table(name = "case_type_schema")
@NoArgsConstructor
@Getter
@EqualsAndHashCode(of = { "id" })
public class CaseTypeSchema implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "case_type")
    private String caseType;

    @Column(name = "stage_type")
    private String stageType;

    @Column(name = "schema_uuid")
    private UUID schemaUuid;

    public CaseTypeSchema(String caseType, String stageType, UUID schemaUuid) {
        this.caseType = caseType;
        this.stageType = stageType;
        this.schemaUuid = schemaUuid;
    }

}
