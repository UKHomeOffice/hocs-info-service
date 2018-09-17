package uk.gov.digital.ho.hocs.info.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "parent_topic")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ParentTopic implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "displayName")
    private String displayName;

    @Column(name = "uuid")
    private UUID uuid;

//    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    @JoinColumn(name = "parent_topic_id", referencedColumnName = "id")
//    private Set<Topic> topic = new HashSet<>();
}
