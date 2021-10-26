package uk.gov.digital.ho.hocs.info.domain.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "case_type_action")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CaseTypeAction {

    @Id
    @Column(name = "uuid")
    private UUID uuid;

    @Column(name = "case_type_uuid")
    private UUID caseTypeUuid;

    @Column(name = "case_type_type")
    private String caseType;

    @Column(name = "action_type")
    private String actionType;

    @Column(name = "action_label")
    private String actionLabel;

    @Column(name = "active")
    private boolean active;

    @Column(name = "max_concurrent_events")
    private int maxConcurrentEvents;

    @Column(name = "props")
    private String props;

    @Column(name = "sort_order")
    private int sortOrder;

    @Column(name = "created_timestamp")
    private LocalDateTime createdTimestamp;

    @Column(name = "last_updated_timestamp")
    private LocalDateTime lastUpdatedTimestamp;
}
