package uk.gov.digital.ho.hocs.info.dto;

import org.junit.Test;
import uk.gov.digital.ho.hocs.info.entities.Minister;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

public class GetMinistersResponseTest {

    UUID uuid = UUID.randomUUID();
    UUID uuid1 = UUID.randomUUID();
    UUID uuid2 = UUID.randomUUID();

    @Test
    public void from() {
        Set<Minister> ministers = new HashSet<>();
        ministers.add( new Minister(1l,"office name 1", "minister name 1", uuid1));
        ministers.add( new Minister(2l,"office name 2", "minister name 2", uuid2));

        GetMinistersResponse getMinistersResponse = GetMinistersResponse.from(ministers);

        List<MinisterDto> responseAsList = new ArrayList<>(Objects.requireNonNull(getMinistersResponse.getMinisters()));

        MinisterDto result1 = responseAsList.stream().filter(x -> "office name 1".equals(x.getOfficeName())).findAny().orElse(null);
        assertThat(result1).isNotNull();
        assertThat(result1.getOfficeName()).isEqualTo("office name 1");
        assertThat(result1.getUuid()).isEqualTo(uuid1);
        MinisterDto result2 = responseAsList.stream().filter(x -> "office name 2".equals(x.getOfficeName())).findAny().orElse(null);
        assertThat(result2).isNotNull();
        assertThat(result2.getOfficeName()).isEqualTo("office name 2");
        assertThat(result2.getUuid()).isEqualTo(uuid2);

    }
}