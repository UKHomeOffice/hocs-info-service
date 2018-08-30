package uk.gov.digital.ho.hocs.info.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "holiday_date")
@NoArgsConstructor
@AllArgsConstructor
public class HolidayDate implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private int id;

    @Column(name = "date")
    @Getter
    @Setter
    private LocalDate date;

}
