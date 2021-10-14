package uk.gov.digital.ho.hocs.info.api.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import uk.gov.digital.ho.hocs.info.domain.model.CaseTypeAction;

import java.util.UUID;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Builder
@Getter
public class CaseTypeActionDto {

    private UUID uuid;
    private UUID caseTypeUuid;
    private String caseType;
    private String actionType;
    private String actionLabel;
    private int sortOrder;
    private boolean active;
    private String supplementaryData;

    public static CaseTypeActionDto from(CaseTypeAction caseTypeAction) {
        return CaseTypeActionDto.builder()
                .uuid(caseTypeAction.getUuid())
                .caseTypeUuid(caseTypeAction.getCaseTypeUuid())
                .caseType(caseTypeAction.getCaseType())
                .actionType(caseTypeAction.getActionType())
                .actionLabel(caseTypeAction.getActionLabel())
                .sortOrder(caseTypeAction.getSortOrder())
                .active(caseTypeAction.isActive())
                .supplementaryData(caseTypeAction.getSupplementaryData())
                .build();
    }
}
