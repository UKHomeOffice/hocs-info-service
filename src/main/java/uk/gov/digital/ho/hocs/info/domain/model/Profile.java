package uk.gov.digital.ho.hocs.info.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "profile")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Profile implements Serializable {

    @Id
    @Column(name = "profile_name")
    private String profileName;

    @Column(name = "parent_system_name")
    private String parentSystemName;

    @Column(name = "summary_deadlines_enabled")
    private boolean summaryDeadlinesEnabled;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OrderBy("id")
    @JoinColumn(name = "profile_name", referencedColumnName = "profile_name")
    private List<SearchField> searchFields;


}
