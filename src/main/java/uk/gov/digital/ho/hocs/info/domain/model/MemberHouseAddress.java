package uk.gov.digital.ho.hocs.info.domain.model;

import javax.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class MemberHouseAddress implements Serializable {

    @Column(name = "full_title")
    private String fullTitle;

    @Column(name = "uuid")
    private UUID uuid;

    @Column(name = "address1", insertable = false)
    private String address1;

    @Column(name = "address2", insertable = false)
    private String address2;

    @Column(name = "address3", insertable = false)
    private String address3;

    @Column(name = "postcode", insertable = false)
    private String postcode;

    @Column(name = "country", insertable = false)
    private String country;

}
