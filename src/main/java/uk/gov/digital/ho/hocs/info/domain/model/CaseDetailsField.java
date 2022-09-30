package uk.gov.digital.ho.hocs.info.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Deprecated(forRemoval = true)
@javax.persistence.Entity
@Table(name = "case_details_field")
@NoArgsConstructor
@AllArgsConstructor
public class CaseDetailsField implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Column(name = "case_type")
    private String caseType;

    @Getter
    @Column(name = "name")
    private String name;

    @Getter
    @Column(name = "component")
    private String component;

    @Getter
    @Column(name = "props")
    private String props;

    @Getter
    @Column(name = "sort_order")
    private Long sortOrder;

}
