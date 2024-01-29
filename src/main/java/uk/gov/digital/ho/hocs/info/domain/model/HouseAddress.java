package uk.gov.digital.ho.hocs.info.domain.model;

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
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "house_address")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class HouseAddress implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "uuid")
    private UUID uuid;

    @Column(name = "house")
    private String house;

    @Column(name = "house_code")
    private String houseCode;

    @Column(name = "address1")
    private String address1;

    @Column(name = "address2")
    private String address2;

    @Column(name = "address3")
    private String address3;

    @Column(name = "postcode")
    private String postcode;

    @Column(name = "country")
    private String country;

    @Column(name = "added")
    private LocalDate added;

    @Column(name = "updated")
    private LocalDate updated;

}