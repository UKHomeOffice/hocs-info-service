package uk.gov.digital.ho.hocs.info.domain.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
