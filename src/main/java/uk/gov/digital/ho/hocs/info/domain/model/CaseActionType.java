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
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "case_action_type")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CaseActionType {

    @Id
    @Column(name = "uuid")
    private UUID uuid;

    @Column(name = "case_type_uuid")
    private UUID caseTypeUuid;

    @Column(name = "case_type_type")
    private String caseType;

    @Column(name = "action_type")
    private String actionType;

    @Column(name = "active")
    private boolean active;

    @Column(name = "supplementary_data")
    private String supplementaryData;

    @Column(name = "sort_order")
    private int sortOrder;

    @Column(name = "created_timestamp")
    private OffsetDateTime createdTimestamp;

    @Column(name = "last_updated_timestamp")
    private OffsetDateTime lastUpdatedTimestamp;
}
