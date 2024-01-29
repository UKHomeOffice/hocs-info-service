package uk.gov.digital.ho.hocs.info.domain.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@AllArgsConstructor()
@NoArgsConstructor()
@Getter
@javax.persistence.Entity
@Table(name = "system_configuration")
@EqualsAndHashCode(of = { "systemName" })
@ToString
public class Configuration implements Serializable {

    @Id
    @Column(name = "system_name")
    private String systemName;

    @Column(name = "display_name")
    private String displayName;

    @Column(name = "bulk_create_enabled")
    private boolean bulkCreateEnabled;

    @Column(name = "view_standard_lines_enabled")
    private boolean viewStandardLinesEnabled;

    @Column(name = "deadlines_enabled")
    private boolean deadlinesEnabled;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OrderBy("id")
    @JoinColumn(name = "parent_system_name", referencedColumnName = "system_name")
    private List<WorkstackType> workstackTypes;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_system_name", referencedColumnName = "system_name")
    private List<Profile> profiles;

    @Column(name = "auto_create_and_allocate_enabled")
    private boolean autoCreateAndAllocateEnabled;

    @Column(name = "read_only_case_view_adapter")
    private String readOnlyCaseViewAdapter;

}
