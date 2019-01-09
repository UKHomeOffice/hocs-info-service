package uk.gov.digital.ho.hocs.info.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "team_contact")
@NoArgsConstructor
@AllArgsConstructor
public class NominatedContact {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private Long id;

    @Getter
    @Column(name = "team_uuid")
    UUID teamUUID;

    @Getter
    @Column(name = "email_address")
    String emailAddress;
}
