package uk.gov.digital.ho.hocs.info.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
public class Team implements Serializable {

    public Team(String displayName, UUID uuid) {
        this.displayName = displayName;
        this.uuid = uuid;
        this.permissions = new HashSet<>();
    }

    public Team(String displayName, UUID uuid, Set<Permission> permissions) {
        this.displayName = displayName;
        this.uuid = uuid;
        this.permissions = Optional.ofNullable(permissions).orElse(new HashSet<>());
    }

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "display_name")
    private String displayName;

    @Column(name = "uuid")
    private UUID uuid;

    @Column(name = "unit_uuid", insertable = false, updatable = false)
    private UUID unitUUID;

    @ManyToOne
    @JoinColumn(name = "unit_uuid", referencedColumnName = "uuid")
    private Unit unit;

    @OneToMany(mappedBy = "team", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private Set<Permission> permissions;

    public void addPermission(Permission permission) {
        permission.setTeam(this);
        permissions.add(permission);
    }

    public void addPermissions(Set<Permission> newPermissions) {
        newPermissions.stream().forEach(permission -> this.addPermission(permission));
    }


}
