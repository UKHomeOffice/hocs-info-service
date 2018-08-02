package uk.gov.digital.ho.hocs.info.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "team")
@NoArgsConstructor
@Getter
public class Team implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "display_name")
    private String displayName;

    @Column(name = "uuid")
    private String uuid;

    @Column(name = "unit_uuid")
    private String unitUUID;

}
