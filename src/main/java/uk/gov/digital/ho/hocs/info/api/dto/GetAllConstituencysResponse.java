package uk.gov.digital.ho.hocs.info.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import uk.gov.digital.ho.hocs.info.domain.model.Constituency;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class GetAllConstituencysResponse {

    @JsonProperty("constituencies")
    List<ConstituencyDto> constituencyDtos;

    public static GetAllConstituencysResponse from(List<Constituency> constituencys) {
        List<ConstituencyDto> constituencyDtos = constituencys.stream().map(ConstituencyDto::from).collect(Collectors.toList());
        return new GetAllConstituencysResponse(constituencyDtos);
    }
}
