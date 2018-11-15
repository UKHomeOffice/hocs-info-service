package uk.gov.digital.ho.hocs.info.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uk.gov.digital.ho.hocs.info.dto.PermissionDto;
import uk.gov.digital.ho.hocs.info.security.AccessLevel;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "permission")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Permission implements Serializable {

    public Permission(AccessLevel accessLevel, Team team, CaseTypeEntity caseType) {
        this.accessLevel = accessLevel;
        this.team = team;
        this.caseType = caseType;
    }

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "team_uuid", insertable = false, updatable = false)
    private UUID teamUUID;

    @Column(name = "case_type", insertable = false, updatable = false)
    private String caseTypeCode;

    @Column(name = "access_level")
    @Enumerated(EnumType.STRING)
    private AccessLevel accessLevel;

    @ManyToOne
    @JoinColumn(name = "team_uuid", referencedColumnName = "uuid")
    private Team team;

    @ManyToOne
    @JoinColumn(name = "case_type", referencedColumnName = "type")
    private CaseTypeEntity caseType;


}
