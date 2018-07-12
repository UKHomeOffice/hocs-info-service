package uk.gov.digital.ho.hocs.info.entities;

import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "topic")
@NoArgsConstructor
public class Topic {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "default_name")
    private String displayName;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "standard_lines_id", referencedColumnName = "id")
    private Set<StandardLines> standardLines = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "template_id", referencedColumnName = "id")
    private Set<Template> templates = new HashSet<>();
}
