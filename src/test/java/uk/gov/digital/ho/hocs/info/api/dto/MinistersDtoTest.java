package uk.gov.digital.ho.hocs.info.api.dto;

import org.junit.Test;
import uk.gov.digital.ho.hocs.info.domain.model.Minister;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class MinistersDtoTest {

    UUID uuid = UUID.randomUUID();

    @Test
    public void from() {
        Minister minister = new Minister(1l,"Home Sec", "bob", uuid);

        MinisterDto ministerDto = MinisterDto.from(minister);

        assertThat(ministerDto.getOfficeName()).isEqualTo("Home Sec");
        assertThat(ministerDto.getUuid()).isEqualTo(uuid);

    }
}