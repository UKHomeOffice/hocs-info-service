package uk.gov.digital.ho.hocs.info.domain.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@AllArgsConstructor()
@NoArgsConstructor()
@Getter
@Entity
@Table(name = "system_configuration")
@EqualsAndHashCode(of = {"systemName"})
public class Configuration implements Serializable {


    @Id
    @Column(name = "system_name")
    private String systemName;

    @Column(name = "display_name")
    private String displayName;

    @Column(name = "document_labels")
    private String documentLabels;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OrderBy("id")
    @JoinColumn(name = "parent_system_name", referencedColumnName = "system_name")
    private List<WorkstackColumn> workstackColumns;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OrderBy("id")
    @JoinColumn(name = "parent_system_name", referencedColumnName = "system_name")
    private List<SearchField> searchFields;

}
