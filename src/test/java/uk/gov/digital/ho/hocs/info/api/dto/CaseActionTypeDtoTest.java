package uk.gov.digital.ho.hocs.info.api.dto;

import org.junit.Test;
import uk.gov.digital.ho.hocs.info.domain.model.CaseActionType;

import java.time.OffsetDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class CaseActionTypeDtoTest {

    private static final UUID rand1 = UUID.randomUUID();
    private static final UUID rand2 = UUID.randomUUID();

    private static final CaseActionType MOCK_ENTITY = new CaseActionType(
            1L,
            rand1,
            rand2,
            "TYPE_1",
            "ACTION_2",
            true,
            "{}",
            10,
            OffsetDateTime.MIN,
            OffsetDateTime.MIN
    );

    @Test
    public void from() {

        CaseActionTypeDto caseActionTypeDto= CaseActionTypeDto.from(MOCK_ENTITY);

        assertEquals(MOCK_ENTITY.getUuid(), caseActionTypeDto.getUuid());
        assertEquals(MOCK_ENTITY.getCaseTypeUuid(), caseActionTypeDto.getCaseTypeUuid());
        assertEquals(MOCK_ENTITY.getCaseType(), caseActionTypeDto.getCaseType());
        assertEquals(MOCK_ENTITY.getActionType(), caseActionTypeDto.getActionType());
        assertEquals(MOCK_ENTITY.getSupplementaryData(), caseActionTypeDto.getSupplementaryData());
        assertEquals(MOCK_ENTITY.isActive(), caseActionTypeDto.isActive());
    }
}