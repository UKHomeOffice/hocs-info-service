package uk.gov.digital.ho.hocs.info.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CreateSomuTypeDto {

    @JsonProperty("caseType")
    private String caseType;

    @JsonProperty("type")
    private String type;

    @JsonProperty("schema")
    private String schema;
}
