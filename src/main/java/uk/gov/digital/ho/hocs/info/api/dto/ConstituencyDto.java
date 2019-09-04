package uk.gov.digital.ho.hocs.info.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.digital.ho.hocs.info.domain.model.Constituency;

import java.util.UUID;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class ConstituencyDto {

    @JsonProperty("label")
    private String constituencyName;

    @JsonProperty("value")
    private UUID uuid;

    public static ConstituencyDto from (Constituency constituency) {
        return new ConstituencyDto(constituency.getConstituencyName(), constituency.getUuid());
    }
}
