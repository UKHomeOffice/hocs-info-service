package uk.gov.digital.ho.hocs.info.entities;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "tenant", schema = "info")
@AllArgsConstructor
@NoArgsConstructor
public class Tenant {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "display_name")
    @Getter
    private String displayName;

    @ManyToMany(mappedBy = "tenants", fetch = FetchType.LAZY)
    private Set<Holiday> holidays = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "tenant_id", referencedColumnName = "id")
    private List<CaseTypeDetail> caseTypeDetails = new ArrayList<>();

//    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
//    @JoinColumn(name = "tenant_id", referencedColumnName = "id")
//    private Set<Unit> unit = new HashSet<>();
}
//
//    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
//    @JoinColumn(name = "tenant_id", referencedColumnName = "id")
//    private Set<ParentTopic> parentTopic = new HashSet<>();
//}
