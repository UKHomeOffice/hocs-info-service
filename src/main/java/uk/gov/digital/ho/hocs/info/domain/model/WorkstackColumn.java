package uk.gov.digital.ho.hocs.info.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@javax.persistence.Entity
@Table(name = "workstack_column")
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class WorkstackColumn implements Serializable {

    @Id
    @Column(name = "uuid")
    private UUID id;

    @Getter
    @Column(name = "display_name")
    private String displayName;

    @Getter
    @Column(name = "data_adapter")
    private String dataAdapter;

    @Getter
    @Column(name = "renderer")
    private String renderer;

    @Getter
    @Column(name = "data_value_key")
    private String dataValueKey;

    @Getter
    @Column(name = "is_filterable")
    private boolean filterable;

    @Getter
    @Column(name = "header_class_name")
    private String headerClassName;

    @Getter
    @Column(name = "sort_strategy")
    private String sortStrategy;

}
