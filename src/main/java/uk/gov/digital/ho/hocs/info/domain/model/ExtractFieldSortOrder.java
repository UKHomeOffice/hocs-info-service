package uk.gov.digital.ho.hocs.info.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "extract_field_sort_order")
@NoArgsConstructor
@AllArgsConstructor
public class ExtractFieldSortOrder implements Serializable {

    @Id
    @Getter
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Column(name = "case_type")
    private String caseType;

    @Getter
    @Column(name = "field_name")
    private String fieldName;

    @Getter
    @Column(name = "sort_order")
    private Long sortOrder;


}
