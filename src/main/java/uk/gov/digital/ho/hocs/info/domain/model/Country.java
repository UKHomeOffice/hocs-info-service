package uk.gov.digital.ho.hocs.info.domain.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@javax.persistence.Entity
@Table(name = "country")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@EqualsAndHashCode
public class Country implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    @Setter
    private String name;

    @Column(name = "is_territory")
    @Setter
    private Boolean isTerritory = false;

    @Column(name = "deleted")
    @Setter
    private Boolean deleted = false;

    public Country(String name, Boolean isTerritory) {
        this.name = name;
        this.isTerritory = isTerritory;
    }

}
