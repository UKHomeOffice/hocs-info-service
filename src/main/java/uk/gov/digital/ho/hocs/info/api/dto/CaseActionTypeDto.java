package uk.gov.digital.ho.hocs.info.api.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import uk.gov.digital.ho.hocs.info.domain.model.CaseActionType;

import java.util.UUID;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Builder
@Getter
public class CaseActionTypeDto {

    private UUID uuid;
    private UUID caseTypeUuid;
    private String caseType;
    private String actionType;
    private int sortOrder;
    private boolean active;
    private String supplementaryData;

    public static CaseActionTypeDto from(CaseActionType caseActionType) {
        return CaseActionTypeDto.builder()
                .uuid(caseActionType.getUuid())
                .caseTypeUuid(caseActionType.getCaseTypeUuid())
                .caseType(caseActionType.getCaseType())
                .actionType(caseActionType.getActionType())
                .sortOrder(caseActionType.getSortOrder())
                .active(caseActionType.isActive())
                .supplementaryData(caseActionType.getSupplementaryData())
                .build();
    }
}
