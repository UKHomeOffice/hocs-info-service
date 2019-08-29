package uk.gov.digital.ho.hocs.info.security;

import org.junit.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


public class Base64UUIDTest {

    @Test
    public void shouldConvertUUIDToBase64AndBack() {
        UUID uuid = UUID.randomUUID();
        String encodedUUID = Base64UUID.UUIDToBase64String(uuid);
        UUID unencodedUUID = Base64UUID.Base64StringToUUID(encodedUUID);
        assertThat(unencodedUUID).isEqualTo(uuid);

    }

    @Test
    public void convertToBase64(){
        String toConvert = "78796659-aa98-4d25-90bb-dd870f0f89ee";

        String result = Base64UUID.UUIDToBase64String(UUID.fromString(toConvert));

        System.out.println(result);

        testConvert("9d9d288b-57ba-4b94-b28a-5b8fdbc5fd38");
        testConvert("01bb5ec7-ef92-47fc-a35a-2be4031fc633");


    }

    @Test
    public void convertToUUID(){
        String toConvert = "9f39_O02SHyLsvzUxsj-SQ";

        String result = Base64UUID.Base64StringToUUID(toConvert).toString();

        System.out.println(result);
    }

    private void testConvert(String toConvert){

        String result = Base64UUID.UUIDToBase64String(UUID.fromString(toConvert));

        System.out.println(toConvert + " - " + result);
    }
}