package uk.gov.digital.ho.hocs.info.security;

import org.junit.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


public class Base64UUIDTest {

    @Test
    public void shouldConvertUUIDToBase64AndBack() {
        UUID uuid = UUID.randomUUID();
        String encodedUUID = Base64UUID.uuidToBase64String(uuid);
        UUID unencodedUUID = Base64UUID.base64StringToUUID(encodedUUID);
        assertThat(unencodedUUID).isEqualTo(uuid);

    }

}