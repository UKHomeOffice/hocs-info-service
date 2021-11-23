package uk.gov.digital.ho.hocs.info.api;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.digital.ho.hocs.info.domain.exception.ApplicationExceptions;
import uk.gov.digital.ho.hocs.info.domain.model.CaseType;
import uk.gov.digital.ho.hocs.info.domain.repository.CaseTypeRepository;
import uk.gov.digital.ho.hocs.info.domain.repository.CaseTypeSchemaRepository;

import java.util.List;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CaseTypeSchemaServiceTest {

    @Mock
    private CaseTypeSchemaRepository caseTypeSchemaRepository;

    @Mock
    private CaseTypeRepository caseTypeRepository;

    private CaseTypeSchemaService caseTypeSchemaService;

    private static final String TEST_CASE_TYPE = "TEST";

    @Before
    public void setUp() {
        this.caseTypeSchemaService = new CaseTypeSchemaService(caseTypeSchemaRepository, caseTypeRepository);
    }

    @Test
    public void shouldReturnStagesForValidCaseType() {
        when(caseTypeRepository.findByType(TEST_CASE_TYPE))
                .thenReturn(new CaseType());

        when(caseTypeSchemaRepository.findDistinctStagesByCaseType(TEST_CASE_TYPE))
                .thenReturn(getMockStageTypes(5));

        List<String> stageTypes = caseTypeSchemaService.getCaseTypeStages(TEST_CASE_TYPE);

        verify(caseTypeRepository, times(1)).findByType(TEST_CASE_TYPE);
        verifyNoMoreInteractions(caseTypeRepository);

        verify(caseTypeSchemaRepository, times(1)).findDistinctStagesByCaseType(TEST_CASE_TYPE);
        verifyNoMoreInteractions(caseTypeSchemaRepository);

        assertThat(stageTypes.size()).isEqualTo(5);
    }

    @Test(expected = ApplicationExceptions.EntityNotFoundException.class)
    public void shouldThrowExceptionForInvalidCaseType() {
        when(caseTypeRepository.findByType(TEST_CASE_TYPE))
                .thenReturn(null);

        caseTypeSchemaService.getCaseTypeStages(TEST_CASE_TYPE);

        verify(caseTypeRepository, times(1)).findByType(TEST_CASE_TYPE);
        verifyNoMoreInteractions(caseTypeRepository);

        verify(caseTypeSchemaRepository, times(0)).findDistinctStagesByCaseType(TEST_CASE_TYPE);
        verifyZeroInteractions(caseTypeSchemaRepository);
    }

    @Test
    public void shouldReturnEmptyStagesForValidCaseType() {
        when(caseTypeRepository.findByType(TEST_CASE_TYPE))
                .thenReturn(new CaseType());

        when(caseTypeSchemaRepository.findDistinctStagesByCaseType(TEST_CASE_TYPE))
                .thenReturn(getZeroMockStageTypes());

        List<String> stageTypes = caseTypeSchemaService.getCaseTypeStages(TEST_CASE_TYPE);

        verify(caseTypeRepository, times(1)).findByType(TEST_CASE_TYPE);
        verifyNoMoreInteractions(caseTypeRepository);

        verify(caseTypeSchemaRepository, times(1)).findDistinctStagesByCaseType(TEST_CASE_TYPE);
        verifyNoMoreInteractions(caseTypeSchemaRepository);

        assertThat(stageTypes.size()).isEqualTo(0);
    }

    private List<String> getZeroMockStageTypes() {
        return getMockStageTypes(0);
    }

    private List<String> getMockStageTypes(int amount) {
        List<String> mockStageTypes = new ArrayList<>();
        for(int i = 0; i < amount; ++i) {
            mockStageTypes.add(String.format("Test_Stage_%d", i));
        }
        return mockStageTypes;
    }

}
