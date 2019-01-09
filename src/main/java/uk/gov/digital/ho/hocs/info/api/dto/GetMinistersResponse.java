package uk.gov.digital.ho.hocs.info.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.digital.ho.hocs.info.domain.model.Minister;

import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor(access =  AccessLevel.PRIVATE)
@Getter
public class GetMinistersResponse {

    @JsonProperty("ministers")
    Set<MinisterDto> ministers;

    public static GetMinistersResponse from(Set<Minister> ministers) {
        Set<MinisterDto> ministerDtos = ministers.stream().map(MinisterDto::from).collect(Collectors.toSet());
        return new GetMinistersResponse(ministerDtos);
    }

}
