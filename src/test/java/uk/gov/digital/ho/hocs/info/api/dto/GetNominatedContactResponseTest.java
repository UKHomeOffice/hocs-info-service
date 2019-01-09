package uk.gov.digital.ho.hocs.info.api.dto;

import org.junit.Test;
import uk.gov.digital.ho.hocs.info.domain.model.NominatedContact;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;


public class GetNominatedContactResponseTest {

    @Test
    public void from() {
        UUID teamUUID = UUID.randomUUID();
        Set<NominatedContact> NominatedContact = new HashSet<>();
        NominatedContact.add(new NominatedContact(1l,teamUUID,"email_address1"));
        NominatedContact.add(new NominatedContact(2l,teamUUID,"email_address2"));

        GetNominatedContactResponse getNominatedContactResponse = GetNominatedContactResponse.from(NominatedContact);

        List<NominatePeopleDto> responseAsList = new ArrayList<>(Objects.requireNonNull(getNominatedContactResponse.getNominatedContactDtos()));

        NominatePeopleDto result1 = responseAsList.stream().filter(x -> "email_address1".equals(x.getEmailAddress())).findAny().orElse(null);
        assertThat(result1).isNotNull();
        NominatePeopleDto result2 = responseAsList.stream().filter(x -> "email_address2".equals(x.getEmailAddress())).findAny().orElse(null);
        assertThat(result2).isNotNull();

    }
}