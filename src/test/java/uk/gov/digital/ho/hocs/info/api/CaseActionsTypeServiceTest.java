package uk.gov.digital.ho.hocs.info.api;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.digital.ho.hocs.info.api.dto.CaseActionTypeDto;
import uk.gov.digital.ho.hocs.info.domain.model.CaseActionType;
import uk.gov.digital.ho.hocs.info.domain.repository.CaseActionTypeRepository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CaseActionsTypeServiceTest {

    @Mock
    private CaseActionTypeRepository caseActionTypeRepository;

    private CaseActionsTypeService caseActionsTypeService;

    private static final String CASE_TYPE = "TYPE_1";

    @Before
    public void setUp() {
        caseActionsTypeService = new CaseActionsTypeService(caseActionTypeRepository);
    }

    @Test
    public void getCaseActionsByType_returnsActions() {

        // GIVEN
        UUID rand1 = UUID.randomUUID();
        UUID rand2 = UUID.randomUUID();
        UUID rand3 = UUID.randomUUID();
        UUID rand4 = UUID.randomUUID();

        CaseActionType mockCaseActionType1 = new CaseActionType(
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

        CaseActionType mockCaseActionType2 = new CaseActionType(
                1L,
                rand3,
                rand4,
                "TYPE_1",
                "ACTION_2",
                true,
                "{}",
                20,
                OffsetDateTime.MIN,
                OffsetDateTime.MIN
        );

        CaseActionTypeDto expectedCaseTypeDto1 = CaseActionTypeDto.from(mockCaseActionType1);
        CaseActionTypeDto expectedCaseTypeDto2 = CaseActionTypeDto.from(mockCaseActionType2);

        when(caseActionTypeRepository.findAllByCaseTypeAndActiveIsTrue(CASE_TYPE)).thenReturn(List.of(mockCaseActionType1, mockCaseActionType2));

        // WHEN
        List<CaseActionTypeDto> output = caseActionsTypeService.getCaseActionsByCaseType(CASE_TYPE);

        // THEN
        assertEquals(expectedCaseTypeDto1.getActionType(), output.get(0).getActionType());
        assertEquals(expectedCaseTypeDto2.getActionType(), output.get(1).getActionType());
        verify(caseActionTypeRepository, times(1)).findAllByCaseTypeAndActiveIsTrue(any());
    }
}