package uk.gov.digital.ho.hocs.info.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.UUID;


@Entity
@Table(name = "minister")
@NoArgsConstructor
@AllArgsConstructor
@Getter


public class Minister implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "office_name")
    private String officeName;

    @Column(name = "minister_name")
    private String ministerName;

    @Column(name = "uuid")
    private UUID uuid;
}
