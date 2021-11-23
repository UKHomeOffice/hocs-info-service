package uk.gov.digital.ho.hocs.info.api;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uk.gov.digital.ho.hocs.info.domain.exception.ApplicationExceptions;

import java.util.List;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CaseTypeSchemaResourceTest {

    @Mock
    private CaseTypeSchemaService caseTypeSchemaService;

    private CaseTypeSchemaResource caseTypeSchemaResource;

    private final static String TEST_CASE_TYPE = "Test";

    @Before
    public void setUp() {
        caseTypeSchemaResource = new CaseTypeSchemaResource(caseTypeSchemaService);
    }

    @Test
    public void shouldReturnStagesForValidCaseType() {
        when(caseTypeSchemaService.getCaseTypeStages(TEST_CASE_TYPE)).thenReturn(getMockStageTypes(5));

        ResponseEntity<List<String>> response =
                caseTypeSchemaResource.getCaseTypeStages(TEST_CASE_TYPE);

        verify(caseTypeSchemaService, times(1)).getCaseTypeStages(eq(TEST_CASE_TYPE));
        verifyNoMoreInteractions(caseTypeSchemaService);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().size()).isEqualTo(5);
    }

    @Test
    public void shouldReturnEmptyStagesForValidCaseType() {
        when(caseTypeSchemaService.getCaseTypeStages(TEST_CASE_TYPE)).thenReturn(getZeroMockStageTypes());

        ResponseEntity<List<String>> response =
                caseTypeSchemaResource.getCaseTypeStages(TEST_CASE_TYPE);

        verify(caseTypeSchemaService, times(1)).getCaseTypeStages(eq(TEST_CASE_TYPE));
        verifyNoMoreInteractions(caseTypeSchemaService);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().size()).isEqualTo(0);
    }

    @Test(expected = ApplicationExceptions.EntityNotFoundException.class)
    public void shouldThrowExceptionForInvalidCaseType() {
        when(caseTypeSchemaService.getCaseTypeStages(TEST_CASE_TYPE))
                .thenThrow(new ApplicationExceptions.EntityNotFoundException(
                        String.format("CaseType %s does not exist.", TEST_CASE_TYPE)));

        caseTypeSchemaResource.getCaseTypeStages(TEST_CASE_TYPE);
        verifyNoMoreInteractions(caseTypeSchemaResource);
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
