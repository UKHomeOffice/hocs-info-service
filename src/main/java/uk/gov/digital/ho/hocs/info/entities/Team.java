package uk.gov.digital.ho.hocs.info.entities;

import lombok.*;
import uk.gov.digital.ho.hocs.info.dto.TeamDto;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "team")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = {"uuid"})
public class Team implements Serializable {

    public Team(String displayName, UUID uuid, boolean active) {
        this.displayName = displayName;
        this.uuid = uuid;
        this.active = active;
        this.permissions = new HashSet<>();
    }

    public Team(String displayName, UUID uuid, Set<Permission> permissions) {
        this.displayName = displayName;
        this.uuid = uuid;
        this.permissions = new HashSet<>();
        if(permissions !=null) {
            addPermissions(permissions);
        }
    }

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "display_name")
    private String displayName;

    @Column(name = "uuid")
    private UUID uuid;

    @Column(name = "active")
    private boolean active;

    @ManyToOne
    @JoinColumn(name = "unit_uuid", referencedColumnName = "uuid")
    private Unit unit;

    @OneToMany(mappedBy = "team", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private Set<Permission> permissions;

    public void addPermission(Permission permission) {
        if(!permissions.contains(permission)) {
            permission.setTeam(this);
            permissions.add(permission);
        }
    }

    public void addPermissions(Set<Permission> newPermissions) {
        newPermissions.stream().forEach(permission -> this.addPermission(permission));
    }


}
