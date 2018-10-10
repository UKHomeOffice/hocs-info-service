package uk.gov.digital.ho.hocs.info.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.digital.ho.hocs.info.entities.Minister;

import java.util.UUID;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class MinisterDto {

    @JsonProperty("label")
    private String officeName;

    @JsonProperty("value")
    private UUID uuid;

    public static MinisterDto from(Minister minister) {
        return new MinisterDto(minister.getOfficeName(), minister.getUuid());
    }
}