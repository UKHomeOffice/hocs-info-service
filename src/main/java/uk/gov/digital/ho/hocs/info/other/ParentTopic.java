package uk.gov.digital.ho.hocs.info.other;

import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "parent-topic")
@NoArgsConstructor
public class ParentTopic {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "default_name")
    private String displayName;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "parent_topic_id", referencedColumnName = "id")
    private Set<Topic> topic = new HashSet<>();
}
