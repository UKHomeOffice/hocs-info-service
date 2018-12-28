package uk.gov.digital.ho.hocs.info.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "template")
@NoArgsConstructor
@AllArgsConstructor
public class Template implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Column(name = "display_name")
    private String displayName;

    @Getter
    @Column(name = "case_type")
    private String caseType;

    @Getter
    @Column(name = "uuid")
    private UUID uuid;

    @Getter
    @Column(name = "deleted")
    private boolean deleted = false;

    public Template(String displayName, String caseType, UUID uuid) {
        this.displayName = displayName;
        this.caseType = caseType;
        this.uuid = uuid;
    }


    public void delete() {
        this.deleted = Boolean.TRUE;

    }
}
