package uk.gov.digital.ho.hocs.info.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.digital.ho.hocs.info.domain.model.NominatedPerson;

import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class GetNominatedPeopleResponse {

    @JsonProperty("nominatedPeople")
    Set<NominatePeopleDto> nominatedPeopleDtos;

    public static GetNominatedPeopleResponse from(Set<NominatedPerson> nominatedPeople) {
        Set<NominatePeopleDto> nominatePeopleDtos = nominatedPeople.stream().map(NominatePeopleDto::from).collect(Collectors.toSet());
        return new GetNominatedPeopleResponse(nominatePeopleDtos);
    }
}
