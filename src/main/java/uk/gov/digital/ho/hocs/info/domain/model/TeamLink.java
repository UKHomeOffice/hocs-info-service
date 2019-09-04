package uk.gov.digital.ho.hocs.info.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "team_link")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class TeamLink implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Setter
    @Column(name = "case_type")
    private String caseType;

    @Setter
    @Column(name = "stage_type")
    private String stageType;

    @Column(name = "link_uuid")
    private UUID linkUUID;

    @Column(name = "link_type")
    private String linkType;

    @Setter
    @Column(name = "responsible_team_uuid")
    private UUID responsibleTeamUUID;

    public TeamLink(UUID linkUUID, String linkType, UUID responsibleTeamUUID, String caseType, String stageType) {
        this.linkUUID = linkUUID;
        this.linkType = linkType;
        this.responsibleTeamUUID = responsibleTeamUUID;
        this.caseType = caseType;
        this.stageType = stageType;
    }
}


