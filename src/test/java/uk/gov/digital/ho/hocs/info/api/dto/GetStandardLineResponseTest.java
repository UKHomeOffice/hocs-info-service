package uk.gov.digital.ho.hocs.info.api.dto;

import org.junit.Test;
import uk.gov.digital.ho.hocs.info.domain.model.StandardLine;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class GetStandardLineResponseTest {

    UUID uuid = UUID.randomUUID();

    @Test
    public void from() {
        StandardLine standardLine = new StandardLine(uuid, "DisplayName", uuid, LocalDateTime.now());

        GetStandardLineResponse getStandardLineResponse = GetStandardLineResponse.from(standardLine);

        assertThat(getStandardLineResponse.getDisplayName()).isEqualTo("DisplayName");
        assertThat(getStandardLineResponse.getUuid()).isEqualTo(uuid);
    }
}