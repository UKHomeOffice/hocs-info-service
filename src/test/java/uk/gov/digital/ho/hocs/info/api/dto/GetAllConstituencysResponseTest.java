package uk.gov.digital.ho.hocs.info.api.dto;

import org.junit.Test;
import uk.gov.digital.ho.hocs.info.domain.model.Constituency;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

public class GetAllConstituencysResponseTest {

    UUID uuid = UUID.randomUUID();

    @Test
    public void from() {
        List<Constituency> constituencyList = new ArrayList<>();
        constituencyList.add(new Constituency(1l, uuid, "Constituency", null, null, true));

        GetAllConstituencysResponse getAllConstituencysResponse = GetAllConstituencysResponse.from(constituencyList);

        assertThat(getAllConstituencysResponse.getConstituencyDtos()).isNotEmpty();
        assertThat(getAllConstituencysResponse.getConstituencyDtos().get(0).getConstituencyName()).isEqualTo("Constituency");
        assertThat(getAllConstituencysResponse.getConstituencyDtos().get(0).getUuid()).isEqualTo(uuid);
    }
}
