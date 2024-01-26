package uk.gov.digital.ho.hocs.info.domain.model;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uk.gov.digital.ho.hocs.info.security.AccessLevel;

import java.io.Serializable;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
@javax.persistence.Entity
@Table(name = "permission")
@EqualsAndHashCode(of = { "accessLevel", "caseType" })
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
