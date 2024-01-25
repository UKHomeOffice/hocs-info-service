package uk.gov.digital.ho.hocs.info.domain.model;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@javax.persistence.Entity
@Table(name = "stage_type")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@EqualsAndHashCode(of = { "type" })
public class StageTypeEntity implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "uuid")
    private UUID uuid;

    @Column(name = "display_name")
    private String displayName;

    @Column(name = "short_code")
    private String shortCode;

    @Column(name = "type")
    private String type;

    @Column(name = "case_type_uuid")
    private UUID caseTypeUUID;

    @Column(name = "deadline")
    private int deadline;

    @Column(name = "deadline_warning")
    private int deadlineWarning;

    @Column(name = "display_stage_order")
    private int displayStageOrder;

    @Column(name = "active")
    private boolean active;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "acting_team_uuid", referencedColumnName = "uuid")
    private Team team;

    @Column(name = "can_display_contributions")
    private boolean canDisplayContributions;

    public StageTypeEntity(UUID uuid,
                           String displayName,
                           String shortCode,
                           String type,
                           UUID caseTypeUUID,
                           int deadline,
                           int deadlineWarning,
                           int displayStageOrder,
                           boolean active,
                           Team team,
                           boolean canDisplayContributions) {
        this.uuid = uuid;
        this.displayName = displayName;
        this.shortCode = shortCode;
        this.type = type;
        this.caseTypeUUID = caseTypeUUID;
        this.deadline = deadline;
        this.deadlineWarning = deadlineWarning;
        this.displayStageOrder = displayStageOrder;
        this.active = active;
        this.team = team;
        this.canDisplayContributions = canDisplayContributions;
    }

}
