package uk.gov.digital.ho.hocs.info.api.dto;

import org.junit.Test;
import uk.gov.digital.ho.hocs.info.domain.model.NominatedPerson;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class NominatePeopleDtoTest {

    UUID uuid = UUID.randomUUID();

    @Test
    public void from() {

        NominatedPerson nominatedPerson = new NominatedPerson(1l, uuid,"email");

        NominatePeopleDto nominatePeopleDto = NominatePeopleDto.from(nominatedPerson);

        assertThat(nominatePeopleDto.getEmailAddress()).isEqualTo("email");
    }
}