package uk.gov.digital.ho.hocs.info.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "topic")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Topic implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "displayName")
    private String displayName;

    @Column(name = "uuid")
    private UUID uuid;

    @Column(name = "parent_topic_uuid")
    private UUID parentTopic;

    @Setter
    @Column(name = "active")
    private Boolean active;

    public Topic(String displayName, UUID parentTopic) {
        this.displayName = displayName;
        this.uuid = UUID.randomUUID();
        this.parentTopic = parentTopic;
        this.active = true;
    }
}


