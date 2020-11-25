package uk.gov.digital.ho.hocs.info.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.digital.ho.hocs.info.domain.model.SomuType;

@AllArgsConstructor
@Getter
public class SomuTypeDto {

    @JsonProperty("caseType")
    private String caseType;

    @JsonProperty("type")
    private String type;

    @JsonProperty("schema")
    private String schema;

    @JsonProperty("active")
    private boolean active;

    public static SomuTypeDto from(SomuType somuType) {

        return new SomuTypeDto(
                somuType.getCaseType(),
                somuType.getType(),
                somuType.getSchema(),
                somuType.isActive());
    }
}
