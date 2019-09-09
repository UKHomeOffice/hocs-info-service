package uk.gov.digital.ho.hocs.info.api.dto;

import org.junit.Test;
import uk.gov.digital.ho.hocs.info.domain.model.Constituency;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class ConstituencyDtoTest {

    UUID uuid = UUID.randomUUID();

    @Test
    public void from() {
        Constituency constituency = new Constituency(1l,uuid,"ConstituencyName",null,null,true);

        ConstituencyDto constituencyDto = ConstituencyDto.from(constituency);

        assertThat(constituencyDto.getConstituencyName()).isEqualTo("ConstituencyName");
        assertThat(constituencyDto.getUuid()).isEqualTo(uuid);
    }
}
