package uk.gov.digital.ho.hocs.info.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "case_type")
@NoArgsConstructor
@Getter
public class CaseType {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "display_name")
    private String displayName;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "case_type_id", referencedColumnName = "id")
    private List<Sla> slas = new ArrayList<>();
}
