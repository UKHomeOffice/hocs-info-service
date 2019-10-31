package uk.gov.digital.ho.hocs.info.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "workstack_column")
@NoArgsConstructor
@AllArgsConstructor
public class WorkstackColumn implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Column(name = "parent_system_name")
    private String parentSystemName;

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

}
