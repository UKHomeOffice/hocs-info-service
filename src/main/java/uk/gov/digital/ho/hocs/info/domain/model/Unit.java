package uk.gov.digital.ho.hocs.info.domain.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@javax.persistence.Entity
@Table(name = "unit")
public class Unit implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Column(name = "uuid", columnDefinition = "uuid")
    private UUID uuid;

    @Getter
    @Column(name = "display_name")
    private String displayName;

    @Getter
    @Column(name = "short_code")
    private String shortCode;

    @Setter
    @Getter
    @Column(name = "active")
    private boolean active;

    @Getter
    @Setter
    @Column(name = "allow_bulk_team_transfer")
    private boolean allowBulkTeamTransfer;

    @Getter
    @OneToMany(mappedBy = "unit", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private Set<Team> teams;

    public Unit(String displayName, String shortCode, boolean active) {
        this.displayName = displayName;
        this.shortCode = shortCode;
        this.uuid = UUID.randomUUID();
        this.active = active;
        this.teams = new HashSet<>(0);
    }

    public void addTeam(Team team) {
        team.setUnit(this);
        teams.add(team);
    }

    public void removeTeam(UUID teamUUID) {
        teams.removeIf(team -> team.getUuid() == teamUUID);

    }

}
