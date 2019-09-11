package uk.gov.digital.ho.hocs.info.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "constituency")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Constituency implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "uuid")
    private UUID uuid;

    @Setter
    @Column(name = "constituency_name")
    private String constituencyName;

    @Column(name = "region_uuid")
    private UUID regionUUID;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "region_uuid", referencedColumnName = "uuid", insertable = false, updatable = false)
    private Region region;

    @Setter
    @Column(name = "active")
    private boolean active;

    public Constituency(String constituencyName) {
        this.constituencyName = constituencyName;
        this.uuid = UUID.randomUUID();
        this.active = true;
    }
}
