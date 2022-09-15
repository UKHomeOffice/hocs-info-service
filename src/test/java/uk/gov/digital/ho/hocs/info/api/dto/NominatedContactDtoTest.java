package uk.gov.digital.ho.hocs.info.api.dto;

import org.junit.Test;
import uk.gov.digital.ho.hocs.info.domain.model.NominatedContact;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class NominatedContactDtoTest {

    UUID uuid = UUID.randomUUID();

    @Test
    public void from() {

        NominatedContact nominatedContact = new NominatedContact(uuid, "email");

        NominatedContactDto nominatedContactDto = NominatedContactDto.from(nominatedContact);

        assertThat(nominatedContactDto.getEmailAddress()).isEqualTo("email");
    }

}