package uk.gov.digital.ho.hocs.info.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "member")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Member implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "display_name")
    private String displayName;

    @Column(name = "letter_name")
    private String letterName;

    @Column(name = "uuid")
    private String uuid;

}
