package uk.gov.digital.ho.hocs.info.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Entity
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
