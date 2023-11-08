package uk.gov.digital.ho.hocs.info.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.UUID;

@jakarta.persistence.Entity
@Table(name = "document_tag")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Deprecated(forRemoval = true)
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
