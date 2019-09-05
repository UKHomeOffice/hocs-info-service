package uk.gov.digital.ho.hocs.info.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "member")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Member implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "house")
    private String house;

    @Column(name = "full_title")
    private String fullTitle;

    @Column(name = "external_reference")
    private String externalReference;

    @Column(name = "uuid")
    private UUID uuid;

    @Column(name = "updated")
    private LocalDateTime updated;

    @Column(name = "deleted")
    @Setter
    private Boolean deleted = false;

    @Column(name = "house_address_uuid")
    private UUID houseAddressUUID;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "house_address_uuid", referencedColumnName = "uuid", insertable = false, updatable = false)
    private HouseAddress houseAddress;

    @Column(name = "constituency_uuid")
    private UUID constituencyUUID;

    @Column(name = "constituency_name")
    private String constituencyName;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "constituency_uuid", referencedColumnName = "uuid", insertable = false, updatable = false)
    private Constituency constituency;

    public Member(String house, String fullTitle, UUID houseAddressUUID,String externalReference ) {
        this.house = house;
        this.fullTitle = toListText(fullTitle);
        this.houseAddressUUID = houseAddressUUID;
        this.externalReference = toListValue(externalReference);
        this.uuid = UUID.randomUUID();
        this.updated = LocalDateTime.now();
    }

    public Member(String house, String fullTitle, UUID houseAddressUUID, String externalReference, UUID constituencyUUID, String constituencyName) {
        this.house = house;
        this.fullTitle = toListText(fullTitle);
        this.houseAddressUUID = houseAddressUUID;
        this.externalReference = toListValue(externalReference);
        this.constituencyUUID = constituencyUUID;
        this.constituencyName = constituencyName;
        this.uuid = UUID.randomUUID();
        this.updated = LocalDateTime.now();
    }

    private static String toListText(String text) {
        text = text.startsWith("\"") ? text.substring(1) : text;
        text = text.endsWith("\"") ? text.substring(0, text.length() - 1) : text;
        return text;
    }

    private static String toListValue(String value) {
        return value.replaceAll(" ", "_")
                .replaceAll("[^a-zA-Z0-9_]+", "")
                .replaceAll("__", "_")
                .toUpperCase();
    }

    public void update(String fullTitle, UUID constituencyUUID, String constituencyName){
        this.fullTitle = fullTitle;
        this.constituencyUUID = constituencyUUID;
        this.constituencyName = constituencyName;
        this.updated = LocalDateTime.now();
    }
}