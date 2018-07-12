package uk.gov.digital.ho.hocs.info.entities;

import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "sla")
@NoArgsConstructor
public class SLA {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "value")
    private int value;

}
