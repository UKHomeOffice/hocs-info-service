package uk.gov.digital.ho.hocs.info.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@jakarta.persistence.Entity
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
    @Setter
    @Column(name = "uuid")
    private UUID uuid;

    @Getter
    @Column(name = "team_uuid")
    UUID teamUUID;

    @Getter
    @Setter
    @Column(name = "email_address")
    String emailAddress;

    public NominatedContact(UUID teamUUID, String emailAddress) {
        this.teamUUID = teamUUID;
        this.uuid = UUID.randomUUID();
        this.emailAddress = emailAddress;
    }

}
