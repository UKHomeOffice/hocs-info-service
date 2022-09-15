package uk.gov.digital.ho.hocs.info.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.digital.ho.hocs.info.domain.model.SomuType;

import java.util.UUID;

@AllArgsConstructor
@Getter
public class SomuTypeDto {

    @JsonProperty("uuid")
    private UUID uuid;

    @JsonProperty("caseType")
    private String caseType;

    @JsonProperty("type")
    private String type;

    @JsonProperty("schema")
    private String schema;

    @JsonProperty("active")
    private boolean active;

    public static SomuTypeDto from(SomuType somuType) {
        return new SomuTypeDto(somuType.getUuid(), somuType.getCaseType(), somuType.getType(), somuType.getSchema(),
            somuType.isActive());
    }

}
