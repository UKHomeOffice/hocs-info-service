package uk.gov.digital.ho.hocs.info.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.digital.ho.hocs.info.entities.MemberHouseAddress;

@AllArgsConstructor(access =  AccessLevel.PRIVATE)
@Getter

public class GetMembersAddressResponse {
    @JsonProperty("type")
    String type;

    @JsonProperty("fullname")
    String fullname;

    @JsonProperty("postcode")
    String postcode;

    @JsonProperty("address1")
    String address1;

    @JsonProperty("address2")
    String address2;

    @JsonProperty("address3")
    String address3;

    @JsonProperty("country")
    String country;

    @JsonProperty("telephone")
    String telephone;

    @JsonProperty("email")
    String email;

    @JsonProperty("reference")
    String reference;

    public static GetMembersAddressResponse from(MemberHouseAddress memberHouseAddress)
    {
        return new GetMembersAddressResponse(
                "Member",
                memberHouseAddress.getFullTitle(),
                memberHouseAddress.getPostcode(),
                memberHouseAddress.getAddress1(),
                memberHouseAddress.getAddress2(),
                memberHouseAddress.getAddress3(),
                memberHouseAddress.getCountry(),
                "",
                "",
                ""
        );
    }
}

