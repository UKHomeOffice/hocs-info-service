//package uk.gov.digital.ho.hocs.info.api.dto;
//
//import org.junit.Test;
//import uk.gov.digital.ho.hocs.info.domain.model.Country;
//
//import java.util.UUID;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//public class CountryDtoTest {
//
//    UUID uuid = UUID.randomUUID();
//
//    @Test
//    public void from() {
//        Country country = new Country(1l, "country", Boolean.FALSE);
//
//        CountryDto countryDto = CountryDto.from(country);
//
//        assertThat(countryDto.getName()).isEqualTo("country");
//    }
//}
