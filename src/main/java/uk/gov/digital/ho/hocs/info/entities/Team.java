package uk.gov.digital.ho.hocs.info.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "team")
@AllArgsConstructor
@Getter
public class Team {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "display_name")
    private String displayName;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "team_id", referencedColumnName = "id")
    private Set<Member> members = new HashSet<>();

//    @ManyToOne(cascade=CascadeType.ALL, fetch = FetchType.EAGER)
//    @JsonIgnoreProperties({"team"}) // prevent infinite recursion (FIXME)
//    private Unit unit;
}
