package uk.gov.digital.ho.hocs.info.api.dto;

import org.junit.Test;
import uk.gov.digital.ho.hocs.info.domain.model.CaseTypeAction;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class CaseTypeActionDtoTest {

    private static final UUID rand1 = UUID.randomUUID();

    private static final UUID rand2 = UUID.randomUUID();

    private static final CaseTypeAction MOCK_ENTITY = new CaseTypeAction(rand1, rand2, "TYPE_1", "ACTION_2",
        "ACTION_SUBTYPE", "ACTION_LABEL", true, 1, "{}", 10, LocalDateTime.MIN, LocalDateTime.MIN);

    @Test
    public void from() {

        CaseTypeActionDto caseTypeActionDto = CaseTypeActionDto.from(MOCK_ENTITY);

        assertEquals(MOCK_ENTITY.getUuid(), caseTypeActionDto.getUuid());
        assertEquals(MOCK_ENTITY.getCaseTypeUuid(), caseTypeActionDto.getCaseTypeUuid());
        assertEquals(MOCK_ENTITY.getCaseType(), caseTypeActionDto.getCaseType());
        assertEquals(MOCK_ENTITY.getActionType(), caseTypeActionDto.getActionType());
        assertEquals(MOCK_ENTITY.getActionLabel(), caseTypeActionDto.getActionLabel());
        assertEquals(MOCK_ENTITY.getProps(), caseTypeActionDto.getProps());
        assertEquals(MOCK_ENTITY.isActive(), caseTypeActionDto.isActive());
    }

}