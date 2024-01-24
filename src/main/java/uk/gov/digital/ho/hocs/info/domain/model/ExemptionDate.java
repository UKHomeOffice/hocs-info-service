package uk.gov.digital.ho.hocs.info.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@jakarta.persistence.Entity
@Table(name = "exemption_date")
@NoArgsConstructor
@AllArgsConstructor
public class ExemptionDate implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private Long id;

    @Column(name = "date")
    @Getter
    @Setter
    private LocalDate date;

}
