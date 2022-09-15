package uk.gov.digital.ho.hocs.info.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.digital.ho.hocs.info.domain.model.Member;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
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

    @JsonProperty("externalKey")
    String externalKey;

    public static GetMembersAddressResponse from(Member member) {
        return new GetMembersAddressResponse("MEMBER", member.getFullTitle(), member.getHouseAddress().getPostcode(),
            member.getHouseAddress().getAddress1(), member.getHouseAddress().getAddress2(),
            member.getHouseAddress().getAddress3(), member.getHouseAddress().getCountry(), "", "", "",
            member.getExternalReference());
    }

}

