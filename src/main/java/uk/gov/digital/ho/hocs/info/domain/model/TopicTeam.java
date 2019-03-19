package uk.gov.digital.ho.hocs.info.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "topic_team")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class TopicTeam implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Setter
    @Column(name = "case_type")
    private String caseType;

    @Setter
    @Column(name = "responsible_team_uuid")
    private UUID responsibleTeamUUID;

    @Column(name = "topic_uuid")
    private UUID topicUUID;

    @Setter
    @Column(name = "stage_type")
    private String stageType;

    public TopicTeam(UUID topicUUID, UUID responsibleTeamUUID, String caseType, String stageType) {
        this.topicUUID = topicUUID;
        this.responsibleTeamUUID = responsibleTeamUUID;
        this.caseType = caseType;
        this.stageType = stageType;
    }
}


