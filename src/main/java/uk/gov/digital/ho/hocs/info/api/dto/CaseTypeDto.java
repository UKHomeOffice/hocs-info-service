package uk.gov.digital.ho.hocs.info.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.digital.ho.hocs.info.domain.model.CaseTypeEntity;

@AllArgsConstructor
@Getter
public class CaseTypeDto {

    @JsonProperty("label")
    private String displayName;

    @JsonProperty("value")
    private String type;

    @JsonProperty("displayCode")
    private String displayCode;

    @JsonProperty("shortCode")
    private String shortCode;

    public static CaseTypeDto from(CaseTypeEntity caseTypeEntity) {

        return new CaseTypeDto(
                caseTypeEntity.getDisplayName(),
                caseTypeEntity.getType(),
                caseTypeEntity.getType(),
                caseTypeEntity.getShortCode());
    }

}
