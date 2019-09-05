package uk.gov.digital.ho.hocs.info.api.dto;

import org.junit.Test;
import uk.gov.digital.ho.hocs.info.domain.model.Constituency;
import uk.gov.digital.ho.hocs.info.domain.model.HouseAddress;
import uk.gov.digital.ho.hocs.info.domain.model.Member;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

public class GetMembersResponseTest {

    UUID uuid = UUID.randomUUID();
    UUID uuid1 = UUID.randomUUID();
    UUID uuid2 = UUID.randomUUID();
    LocalDateTime localDateTime = LocalDateTime.of(2018, 1, 1, 12, 01);

    @Test
    public void from() {
        HouseAddress houseAddress = new HouseAddress(1l, uuid, "Lords", "LOR", "address1", "address2", "address3", "s1 1dj", "United Kingdom", LocalDate.of(2017, 1, 1), LocalDate.of(2017, 1, 1));
        Member member1 = new Member(1l, "Lords", "Lord Bob", "REF1", uuid1, localDateTime, false, uuid, houseAddress, UUID.randomUUID(), "constituency", new Constituency());
        Member member2 = new Member(2l, "Lords", "Sir Fred", "REF2", uuid2, localDateTime, false, uuid, houseAddress, UUID.randomUUID(), "constituency", new Constituency());

        Set<Member> members = new HashSet<>();
        members.add(member1);
        members.add(member2);

        GetMembersResponse getMembersResponse = GetMembersResponse.from(members);

        List<MemberDto> responseAsList = new ArrayList<>(Objects.requireNonNull(getMembersResponse.getMembers()));

        MemberDto result1 = responseAsList.stream().filter(x -> "Lord Bob".equals(x.getDisplayName())).findAny().orElse(null);
        assertThat(result1).isNotNull();
        assertThat(result1.getHouse()).isEqualTo("Lords");
        assertThat(result1.getUuid()).isEqualTo(uuid1);
        MemberDto result2 = responseAsList.stream().filter(x -> "Sir Fred".equals(x.getDisplayName())).findAny().orElse(null);
        assertThat(result2).isNotNull();
        assertThat(result2.getHouse()).isEqualTo("Lords");
        assertThat(result2.getUuid()).isEqualTo(uuid2);

    }
}