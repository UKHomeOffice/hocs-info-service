package uk.gov.digital.ho.hocs.info.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@jakarta.persistence.Entity
@Table(name = "parent_topic")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ParentTopic implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "uuid")
    private UUID uuid;

    @Column(name = "display_name")
    private String displayName;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_topic_uuid", referencedColumnName = "uuid")
    private Set<Topic> topic = new HashSet<>();

    @Setter
    @Column(name = "active")
    private boolean active;

    public ParentTopic(String displayName) {
        this.displayName = displayName;
        this.uuid = UUID.randomUUID();
        this.active = true;
    }

    public ParentTopic(String displayName, Boolean active) {
        this.displayName = displayName;
        this.uuid = UUID.randomUUID();
        this.active = active;
    }

}
