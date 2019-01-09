package uk.gov.digital.ho.hocs.info.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.digital.ho.hocs.info.domain.model.StageTypeEntity;

@AllArgsConstructor
@Getter
public class StageTypeDto {

    //TODO: remove - used in UI
    @JsonProperty("label")
    private String label;

    //TODO: remove - used in UI
    @JsonProperty("value")
    private String value;

    @JsonProperty("displayName")
    private String displayName;

    @JsonProperty("shortCode")
    private String shortCode;

    @JsonProperty("type")
    private String type;

    public static StageTypeDto from(StageTypeEntity stageTypeEntity) {

        return new StageTypeDto(
                stageTypeEntity.getDisplayName(),
                stageTypeEntity.getType(),
                stageTypeEntity.getDisplayName(),
                stageTypeEntity.getShortCode(),
                stageTypeEntity.getType());
    }

}
