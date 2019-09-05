package uk.gov.digital.ho.hocs.info.api.dto;

import org.junit.Test;
import uk.gov.digital.ho.hocs.info.domain.model.Constituency;
import uk.gov.digital.ho.hocs.info.domain.model.HouseAddress;
import uk.gov.digital.ho.hocs.info.domain.model.Member;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


public class GetMembersAddressResponseTest {

    UUID uuid = UUID.randomUUID();
    LocalDateTime localDateTime = LocalDateTime.of(2018, 1, 1, 12, 01);

    @Test
    public void from() {
        HouseAddress houseAddress = new HouseAddress(1l, uuid, "Lords", "LOR", "address1", "address2", "address3", "s1 1dj", "United Kingdom", LocalDate.of(2017, 1, 1), LocalDate.of(2017, 1, 1));
        Member member = new Member(1l, "Lords", "Bob", "REF1", uuid, localDateTime, false, uuid, houseAddress, UUID.randomUUID(), "constituency", new Constituency());

        GetMembersAddressResponse getMembersAddressResponse = GetMembersAddressResponse.from(member);

        assertThat(getMembersAddressResponse.getType()).isEqualTo("MEMBER");
        assertThat(getMembersAddressResponse.getFullname()).isEqualTo("Bob");
        assertThat(getMembersAddressResponse.getPostcode()).isEqualTo("s1 1dj");
        assertThat(getMembersAddressResponse.getAddress1()).isEqualTo("address1");
        assertThat(getMembersAddressResponse.getAddress2()).isEqualTo("address2");
        assertThat(getMembersAddressResponse.getAddress3()).isEqualTo("address3");
        assertThat(getMembersAddressResponse.getCountry()).isEqualTo("United Kingdom");
        assertThat(getMembersAddressResponse.getTelephone()).isNullOrEmpty();
        assertThat(getMembersAddressResponse.getEmail()).isNullOrEmpty();
        assertThat(getMembersAddressResponse.getReference()).isNullOrEmpty();




    }
}