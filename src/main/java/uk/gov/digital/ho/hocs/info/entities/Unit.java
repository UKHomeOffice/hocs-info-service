package uk.gov.digital.ho.hocs.info.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "unit")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Unit implements Serializable {

    public Unit(String displayName, String shortCode, UUID uuid, boolean active) {
        this.displayName = displayName;
        this.shortCode = shortCode;
        this.uuid = uuid;
        this.active = active;
    }

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "display_name")
    private String displayName;

    @Column(name = "short_code")
    private String shortCode;

    @Column(name = "uuid")
    private UUID uuid;

    @Column(name = "active")
    private boolean active;

    @OneToMany(mappedBy = "unit", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private Set<Team> teams;

    public void addTeam(Team team) {
        team.setUnit(this);
        teams.add(team);
    }
    public void removeTeam(UUID teamUUID) {
        teams.removeIf(team -> team.getUnitUUID() == teamUUID);

    }

}
