package uk.gov.digital.ho.hocs.info.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@javax.persistence.Entity
@Table(name = "document_tag")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class DocumentTag implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "uuid")
    private UUID uuid;

    @Column(name = "case_type_uuid")
    private UUID caseTypeUUID;

    @Column(name = "tag")
    private String tag;

    @Column(name = "sort_order")
    private short sortOrder;
}
