package uk.gov.digital.ho.hocs.info.api.dto;

import org.junit.Test;
import uk.gov.digital.ho.hocs.info.domain.model.Constituency;
import uk.gov.digital.ho.hocs.info.domain.model.HouseAddress;
import uk.gov.digital.ho.hocs.info.domain.model.Member;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class MemberDtoTest {

    UUID uuid = UUID.randomUUID();
    UUID houseUUID = UUID.randomUUID();

    @Test
    public void from() {
        HouseAddress houseAddress = new HouseAddress(1l, uuid, "house", "housecode", "address1", "address2", "address3", "postcode", "counter", LocalDate.now(), LocalDate.now());
        Member member = new Member(1l, "House", "Full Title", "Ext Ref", uuid, LocalDateTime.now(), Boolean.FALSE, houseUUID, houseAddress, UUID.randomUUID(), "constituency", new Constituency());

        MemberDto memberDto = MemberDto.from(member);

        assertThat(memberDto.getDisplayName()).isEqualTo("Full Title");
        assertThat(memberDto.getUuid()).isEqualTo(uuid);
        assertThat(memberDto.getHouse()).isEqualTo("House");
    }
}