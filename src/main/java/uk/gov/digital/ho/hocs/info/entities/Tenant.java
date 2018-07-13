package uk.gov.digital.ho.hocs.info.entities;


import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "tenant", schema = "info")
@NoArgsConstructor
public class Tenant {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "display_name")
    private String displayName;

    @ManyToMany(mappedBy = "tenants", fetch = FetchType.LAZY)
    private Set<Holiday> holidays = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "tenant_id", referencedColumnName = "id")
    private List<CaseType> caseTypes = new ArrayList<>();
}




//
//    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
//    @JoinColumn(name = "tenant_id", referencedColumnName = "id")
//    private Set<Unit> units = new HashSet<>();
//
//    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
//    @JoinColumn(name = "tenant_id", referencedColumnName = "id")
//    private Set<ParentTopic> parentTopic = new HashSet<>();
//}
