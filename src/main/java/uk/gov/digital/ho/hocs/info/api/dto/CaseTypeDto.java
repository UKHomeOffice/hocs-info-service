package uk.gov.digital.ho.hocs.info.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import uk.gov.digital.ho.hocs.info.domain.model.CaseType;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CaseTypeDto {

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

    @JsonProperty("previousCaseType")
    private String previousCaseType;

    @JsonProperty("sla")
    private int sla;

    @JsonProperty("deadLineWarning")
    private int deadLineWarning;

    public static CaseTypeDto from(CaseType caseType) {

        return new CaseTypeDto(
                caseType.getDisplayName(),
                caseType.getType(),
                caseType.getDisplayName(),
                caseType.getShortCode(),
                caseType.getType(),
                caseType.getPreviousCaseType(),
                caseType.getDeadlineStageEntity().getDeadline(),
                caseType.getDeadlineStageEntity().getDeadlineWarning()
        );
    }

}
