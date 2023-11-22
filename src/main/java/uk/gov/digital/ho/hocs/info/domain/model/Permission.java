package uk.gov.digital.ho.hocs.info.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uk.gov.digital.ho.hocs.info.security.AccessLevel;

import java.io.Serializable;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
@jakarta.persistence.Entity
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
