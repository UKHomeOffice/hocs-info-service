package uk.gov.digital.ho.hocs.info.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "correspondent_type")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CorrespondentType implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "uuid")
    private UUID uuid;

    @Column(name = "display_name")
    private String displayName;

    @Column(name = "type")
    private String type;

    public CorrespondentType(String displayName, String type) {
        this.uuid = UUID.randomUUID();
        this.displayName = displayName;
        this.type = type;
    }

}
