package uk.gov.digital.ho.hocs.info.other;

import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "standard-lines")
@NoArgsConstructor
public class StandardLines {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "display_name")
    private String displayName;

    @Column(name = "s3_link")
    private String s3Link;
}
