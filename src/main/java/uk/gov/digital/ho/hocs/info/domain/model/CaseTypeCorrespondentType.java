package uk.gov.digital.ho.hocs.info.domain.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@javax.persistence.Entity
@Table(name = "case_type_correspondent_type")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CaseTypeCorrespondentType implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "case_type_uuid")
    private UUID caseTypeUUID;

    @Column(name = "correspondent_type_uuid")
    private UUID correspondentTypeUUID;

}
