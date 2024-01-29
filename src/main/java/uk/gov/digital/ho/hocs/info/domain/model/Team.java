package uk.gov.digital.ho.hocs.info.domain.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@javax.persistence.Entity
@Table(name = "team")
@EqualsAndHashCode(of = { "uuid" })
public class Team implements Serializable {

    public Team(String displayName, boolean active) {
        this.displayName = displayName;
        this.uuid = UUID.randomUUID();
        this.active = active;
        this.permissions = new HashSet<>(0);
    }

    public Team(String displayName, Set<Permission> permissions) {
        this.displayName = displayName;
        this.uuid = UUID.randomUUID();
        this.permissions = new HashSet<>(permissions.size());
        addPermissions(permissions);
    }

    public Team(String displayName, Unit unit, boolean active) {
        this.displayName = displayName;
        this.uuid = UUID.randomUUID();
        this.active = active;
        this.permissions = new HashSet<>(0);
        this.unit = unit;
    }

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Column(name = "uuid", columnDefinition = "uuid")
    private UUID uuid;

    @Setter
    @Getter
    @Column(name = "display_name")
    private String displayName;

    @Setter
    @Getter
    @Column(name = "letter_name")
    private String letterName;

    @Setter
    @Getter
    @Column(name = "active")
    private boolean active;

    @Setter
    @Getter
    @ManyToOne
    @JoinColumn(name = "unit_uuid", referencedColumnName = "uuid")
    private Unit unit;

    @Getter
    @OneToMany(mappedBy = "team", orphanRemoval = true, cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private Set<Permission> permissions;

    public void addPermission(Permission permission) {
        if (!permissions.contains(permission)) {
            permission.setTeam(this);
            permissions.add(permission);
        }
    }

    public void deletePermission(Permission permission) {
        permissions.remove(permission);
    }

    public void addPermissions(Set<Permission> newPermissions) {
        newPermissions.forEach(this::addPermission);
    }

    public void deletePermissions(Set<Permission> newPermissions) {
        newPermissions.forEach(this::deletePermission);
    }

}
