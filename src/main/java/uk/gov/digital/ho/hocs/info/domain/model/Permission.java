package uk.gov.digital.ho.hocs.info.domain.model;

import lombok.*;
import uk.gov.digital.ho.hocs.info.security.AccessLevel;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "permission")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"accessLevel", "caseType", "team"})
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
