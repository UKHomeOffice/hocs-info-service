package uk.gov.digital.ho.hocs.info.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Entity
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
