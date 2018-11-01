package uk.gov.digital.ho.hocs.info.dto;

import org.junit.Test;
import uk.gov.digital.ho.hocs.info.entities.NominatedPerson;
import uk.gov.digital.ho.hocs.info.entities.Team;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

public class NominatePeopleDtoTest {

    UUID uuid = UUID.randomUUID();

    @Test
    public void from() {

        NominatedPerson nominatedPerson = new NominatedPerson(1l, uuid,"email");

        NominatePeopleDto nominatePeopleDto = NominatePeopleDto.from(nominatedPerson);

        assertThat(nominatePeopleDto.getEmailAddress()).isEqualTo("email");
    }
}