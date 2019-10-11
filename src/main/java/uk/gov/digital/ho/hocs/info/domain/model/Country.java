package uk.gov.digital.ho.hocs.info.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "country")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Country implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    @Setter
    private String name;

    @Column(name = "deleted")
    @Setter
    private Boolean deleted = false;

    public Country(String name) {
        this.name = name;
    }
}
