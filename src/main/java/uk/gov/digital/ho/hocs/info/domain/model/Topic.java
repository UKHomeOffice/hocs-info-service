package uk.gov.digital.ho.hocs.info.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@jakarta.persistence.Entity
@Table(name = "topic")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Topic implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Setter
    @Column(name = "displayName")
    private String displayName;

    @Column(name = "uuid")
    private UUID uuid;

    @Setter
    @Column(name = "parent_topic_uuid")
    private UUID parentTopic;

    @Setter
    @Column(name = "active")
    private boolean active;

    public Topic(String displayName, UUID parentTopic) {
        this.displayName = displayName;
        this.uuid = UUID.randomUUID();
        this.parentTopic = parentTopic;
        this.active = true;
    }

}


