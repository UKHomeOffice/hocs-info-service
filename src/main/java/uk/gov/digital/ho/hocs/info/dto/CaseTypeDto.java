package uk.gov.digital.ho.hocs.info.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.digital.ho.hocs.info.entities.CaseTypeEntity;


@AllArgsConstructor
@Getter
public class CaseTypeDto {

    @JsonProperty("displayName")
    private String displayName;

    @JsonProperty("type")
    private String type;

    public static CaseTypeDto from(CaseTypeEntity caseTypeEntity){
        return new CaseTypeDto(caseTypeEntity.getDisplayName(), caseTypeEntity.getType());
    }

}
