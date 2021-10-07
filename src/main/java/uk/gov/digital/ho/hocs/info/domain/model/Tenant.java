package uk.gov.digital.ho.hocs.info.domain.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@javax.persistence.Entity
@Table(name = "tenant")
@AllArgsConstructor
@NoArgsConstructor
public class Tenant implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "display_name")
    @Getter
    private String displayName;
}
