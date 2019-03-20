package uk.gov.digital.ho.hocs.info.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import uk.gov.digital.ho.hocs.info.domain.model.CorrespondentType;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CorrespondentTypeDto {

    @JsonProperty("uuid")
    private UUID uuid;

    @JsonProperty("displayName")
    private String displayName;

    @JsonProperty("type")
    private String type;

    public static CorrespondentTypeDto from(CorrespondentType correspondentType) {
        return new CorrespondentTypeDto(
                correspondentType.getUuid(),
                correspondentType.getDisplayName(),
                correspondentType.getType());
    }

}

