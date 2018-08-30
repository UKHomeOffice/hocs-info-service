package uk.gov.digital.ho.hocs.info.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "template")
@NoArgsConstructor
public class Template implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Getter
    @Column(name = "display_name")
    private String displayName;

    @Getter
    @Column(name = "case_type")
    private String caseType;

    @Getter
    @Column(name = "file_link")
    private String fileLink;

    @Column(name = "uuid")
    private UUID uuid;

    @Column(name = "active")
    private boolean active;
}
