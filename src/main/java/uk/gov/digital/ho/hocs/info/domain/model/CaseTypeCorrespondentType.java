package uk.gov.digital.ho.hocs.info.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@jakarta.persistence.Entity
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
