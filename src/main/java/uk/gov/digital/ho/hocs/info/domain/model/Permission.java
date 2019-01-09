package uk.gov.digital.ho.hocs.info.domain.model;

import lombok.*;
import uk.gov.digital.ho.hocs.info.security.AccessLevel;

import javax.persistence.*;
import java.io.Serializable;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
@Entity
@Table(name = "permission")
@EqualsAndHashCode(of = {"accessLevel", "caseType", "team"})

public class Permission implements Serializable {

    public Permission(AccessLevel accessLevel, Team team, CaseType caseType) {
        this.accessLevel = accessLevel;
        this.team = team;
        this.caseType = caseType;
    }

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Getter
    @ManyToOne
    @JoinColumn(name = "team_uuid", referencedColumnName = "uuid")
    private Team team;

    @Getter
    @ManyToOne
    @JoinColumn(name = "case_type", referencedColumnName = "type")
    private CaseType caseType;

    @Getter
    @Column(name = "access_level")
    @Enumerated(EnumType.STRING)
    private AccessLevel accessLevel;


}
