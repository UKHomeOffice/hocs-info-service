package uk.gov.digital.ho.hocs.info.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.digital.ho.hocs.info.domain.model.NominatedContact;

import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class GetNominatedContactResponse {

    @JsonProperty("nominatedContact")
    Set<NominatePeopleDto> nominatedContactDtos;

    public static GetNominatedContactResponse from(Set<NominatedContact> nominatedContacts) {
        Set<NominatePeopleDto> nominatedcontactDtos = nominatedContacts.stream().map(NominatePeopleDto::from).collect(Collectors.toSet());
        return new GetNominatedContactResponse(nominatedcontactDtos);
    }
}
