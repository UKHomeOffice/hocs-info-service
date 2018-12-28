package uk.gov.digital.ho.hocs.info.api.dto;

import org.junit.Test;
import uk.gov.digital.ho.hocs.info.domain.model.NominatedPerson;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;


public class GetNominatedPeopleResponseTest {

    @Test
    public void from() {
        UUID teamUUID = UUID.randomUUID();
        Set<NominatedPerson> nominatedPeople = new HashSet<>();
        nominatedPeople.add(new NominatedPerson(1l,teamUUID,"email_address1"));
        nominatedPeople.add(new NominatedPerson(2l,teamUUID,"email_address2"));

        GetNominatedPeopleResponse getNominatedPeopleResponse = GetNominatedPeopleResponse.from(nominatedPeople);

        List<NominatePeopleDto> responseAsList = new ArrayList<>(Objects.requireNonNull(getNominatedPeopleResponse.getNominatedPeopleDtos()));

        NominatePeopleDto result1 = responseAsList.stream().filter(x -> "email_address1".equals(x.getEmailAddress())).findAny().orElse(null);
        assertThat(result1).isNotNull();
        NominatePeopleDto result2 = responseAsList.stream().filter(x -> "email_address2".equals(x.getEmailAddress())).findAny().orElse(null);
        assertThat(result2).isNotNull();

    }
}