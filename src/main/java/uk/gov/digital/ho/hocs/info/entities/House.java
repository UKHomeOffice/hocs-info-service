package uk.gov.digital.ho.hocs.info.entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uk.gov.digital.ho.hocs.info.entities.Member;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@Entity
@Table(name = "houses")
@Access(AccessType.FIELD)
@EqualsAndHashCode(of = {"name"})
public class House implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    @Getter
    private String name;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name ="house_id", referencedColumnName = "id")
    @Getter
    @Setter
    private Set<Member> members = new HashSet<>();

    @Column(name = "deleted", nullable = false)
    @Getter
    @Setter
    private Boolean deleted = false;

    public House(String name, Set<Member> members) {
        this.name = name;
        this.members = members;
    }
}