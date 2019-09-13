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

    @JsonProperty("uuid")
    private UUID uuid;

    @JsonProperty("constituencyName")
    private String constituencyName;

    @JsonProperty("regionUUID")
    private UUID regionUUID;

    @JsonProperty("active")
    private boolean active;

    public static ConstituencyDto from (Constituency constituency) {
        return new ConstituencyDto(constituency.getUuid(), constituency.getConstituencyName(), constituency.getRegionUUID(), constituency.isActive());
    }
}
