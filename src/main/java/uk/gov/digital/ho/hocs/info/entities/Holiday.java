package uk.gov.digital.ho.hocs.info.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "holiday")
@NoArgsConstructor
@AllArgsConstructor
public class Holiday {

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

    @ManyToMany()
    @JoinTable(
            name = "tenants_holidays",
            joinColumns = { @JoinColumn(name = "tenant_id") },
            inverseJoinColumns = { @JoinColumn(name = "holiday_id") }
    )
    private List<Tenant> tenants = new ArrayList<>();

}
