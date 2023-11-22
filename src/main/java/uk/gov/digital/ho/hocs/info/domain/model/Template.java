package uk.gov.digital.ho.hocs.info.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@jakarta.persistence.Entity
@Table(name = "template")
@NoArgsConstructor
@AllArgsConstructor
public class Template implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Column(name = "uuid")
    private UUID uuid;

    @Getter
    @Setter
    @Column(name = "document_uuid")
    private UUID documentUUID;

    @Getter
    @Column(name = "display_name")
    private String displayName;

    @Getter
    @Column(name = "case_type")
    private String caseType;

    @Getter
    @Column(name = "deleted")
    private boolean deleted = false;

    public Template(String displayName, String caseType) {
        this.uuid = UUID.randomUUID();
        this.displayName = displayName;
        this.caseType = caseType;
    }

    public void delete() {
        this.deleted = Boolean.TRUE;

    }

}
