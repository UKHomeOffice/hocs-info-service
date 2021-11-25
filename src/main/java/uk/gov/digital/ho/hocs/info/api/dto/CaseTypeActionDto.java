package uk.gov.digital.ho.hocs.info.api.dto;

import lombok.*;
import uk.gov.digital.ho.hocs.info.domain.model.CaseTypeAction;

import java.util.UUID;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Builder
@Getter
@ToString
public class CaseTypeActionDto {

    private UUID uuid;
    private UUID caseTypeUuid;
    private String caseType;
    private String actionType;
    private String actionSubtype;
    private String actionLabel;
    private int maxConcurrentEvents;
    private int sortOrder;
    private boolean active;
    private String props;

    public static CaseTypeActionDto from(CaseTypeAction caseTypeAction) {
        return CaseTypeActionDto.builder()
                .uuid(caseTypeAction.getUuid())
                .caseTypeUuid(caseTypeAction.getCaseTypeUuid())
                .caseType(caseTypeAction.getCaseType())
                .actionType(caseTypeAction.getActionType())
                .actionSubtype(caseTypeAction.getActionSubtype())
                .actionLabel(caseTypeAction.getActionLabel())
                .maxConcurrentEvents(caseTypeAction.getMaxConcurrentEvents())
                .sortOrder(caseTypeAction.getSortOrder())
                .active(caseTypeAction.isActive())
                .props(caseTypeAction.getProps())
                .build();
    }
}
