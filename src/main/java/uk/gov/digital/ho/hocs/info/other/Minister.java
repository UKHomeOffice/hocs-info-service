package uk.gov.digital.ho.hocs.info.other;

import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "minister")
@NoArgsConstructor
public class Minister {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "display_name")
    private String displayName;
}
