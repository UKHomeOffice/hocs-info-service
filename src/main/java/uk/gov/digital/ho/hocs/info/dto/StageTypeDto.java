package uk.gov.digital.ho.hocs.info.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.digital.ho.hocs.info.entities.StageTypeEntity;

@AllArgsConstructor
@Getter
public class StageTypeDto {

    @JsonProperty("label")
    private String displayName;

    @JsonProperty("value")
    private String type;

    @JsonProperty("displayCode")
    private String displayCode;

    @JsonProperty("shortCode")
    private String shortCode;

    public static StageTypeDto from(StageTypeEntity stageTypeEntity) {

        return new StageTypeDto(
                stageTypeEntity.getDisplayName(),
                stageTypeEntity.getType(),
                stageTypeEntity.getType(),
                stageTypeEntity.getShortCode());
    }

}
