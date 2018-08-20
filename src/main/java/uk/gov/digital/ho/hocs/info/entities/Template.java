package uk.gov.digital.ho.hocs.info.entities;

import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "template")
@NoArgsConstructor
public class Template implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "display_name")
    private String displayName;

    @Column(name = "s3_link")
    private String s3Link;
}
