package uk.gov.digital.ho.hocs.info.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "standard_lines")
@NoArgsConstructor
public class StandardLines implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Getter
    @Column(name = "display_name")
    private String displayName;

    @Getter
    @Column(name = "document_key")
    private String documentKey;

    @Column(name = "uuid")
    private UUID uuid;
}
