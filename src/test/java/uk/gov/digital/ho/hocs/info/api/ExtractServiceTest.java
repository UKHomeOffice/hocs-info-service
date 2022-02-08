package uk.gov.digital.ho.hocs.info.api;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.digital.ho.hocs.info.domain.model.ExtractFieldSortOrder;
import uk.gov.digital.ho.hocs.info.domain.model.Field;
import uk.gov.digital.ho.hocs.info.domain.repository.ExtractFieldSortOrderRepository;
import uk.gov.digital.ho.hocs.info.security.AccessLevel;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ExtractServiceTest {

    @Mock
    private ExtractFieldSortOrderRepository extractFieldSortOrderRepository;
    @Mock
    private SchemaService schemaService;

    private ExtractService service;

    private static final String CASE_TYPE = "CASE_TYPE";
    private static final String FIELD_EXT_ONLY_1 = "FieldExt1";
    private static final String FIELD_EXT_ONLY_2 = "FieldExt2";
    private static final String FIELD_EXT_ONLY_3 = "FieldExt3";
    private static final String FIELD_1 = "Field1";
    private static final String FIELD_2 = "Field2";
    private static final String FIELD_3 = "Field3";
    private static final String FIELD_4 = "Field4";
    private static final String FIELD_5 = "Field5";
    private static final String FIELD_6 = "Field6";

    @Before
    public void setup() {
        service = new ExtractService(extractFieldSortOrderRepository, schemaService);
        mockFields();
    }

    @Test
    public void getAllReportingFieldsForCaseType_noSort() {
        List<String> results = service.getAllReportingFieldsForCaseType(CASE_TYPE);

        assertThat(results.size()).isEqualTo(9);
        assertThat(results.get(0)).isEqualTo(FIELD_EXT_ONLY_1);
        assertThat(results.get(1)).isEqualTo(FIELD_EXT_ONLY_2);
        assertThat(results.get(2)).isEqualTo(FIELD_EXT_ONLY_3);
        assertThat(results.get(3)).isEqualTo(FIELD_1);
        assertThat(results.get(4)).isEqualTo(FIELD_2);
        assertThat(results.get(5)).isEqualTo(FIELD_3);
        assertThat(results.get(6)).isEqualTo(FIELD_4);
        assertThat(results.get(7)).isEqualTo(FIELD_5);
        assertThat(results.get(8)).isEqualTo(FIELD_6);

        verify(schemaService).getExtractOnlyFields();
        verify(schemaService).getAllReportingFieldsForCaseType(CASE_TYPE);
        verify(extractFieldSortOrderRepository).findAllByCaseTypeOrderBySortOrder(CASE_TYPE);
        verifyNoMoreInteractions(schemaService, extractFieldSortOrderRepository);
    }

    @Test
    public void getAllReportingFieldsForCaseType_partialSort() {
        List<ExtractFieldSortOrder> testSortOrder = new ArrayList<>();
        testSortOrder.add(buildSortOrder(FIELD_3, 1L));
        testSortOrder.add(buildSortOrder(FIELD_4, 2L));
        testSortOrder.add(buildSortOrder(FIELD_5, 3L));
        when(extractFieldSortOrderRepository.findAllByCaseTypeOrderBySortOrder(CASE_TYPE)).thenReturn(testSortOrder);

        List<String> results = service.getAllReportingFieldsForCaseType(CASE_TYPE);

        assertThat(results.size()).isEqualTo(9);
        assertThat(results.get(0)).isEqualTo(FIELD_3);
        assertThat(results.get(1)).isEqualTo(FIELD_4);
        assertThat(results.get(2)).isEqualTo(FIELD_5);
        assertThat(results.get(3)).isEqualTo(FIELD_EXT_ONLY_1);
        assertThat(results.get(4)).isEqualTo(FIELD_EXT_ONLY_2);
        assertThat(results.get(5)).isEqualTo(FIELD_EXT_ONLY_3);
        assertThat(results.get(6)).isEqualTo(FIELD_1);
        assertThat(results.get(7)).isEqualTo(FIELD_2);
        assertThat(results.get(8)).isEqualTo(FIELD_6);

        verify(schemaService).getExtractOnlyFields();
        verify(schemaService).getAllReportingFieldsForCaseType(CASE_TYPE);
        verify(extractFieldSortOrderRepository).findAllByCaseTypeOrderBySortOrder(CASE_TYPE);
        verifyNoMoreInteractions(schemaService, extractFieldSortOrderRepository);
    }

    @Test
    public void getAllReportingFieldsForCaseType_completeSort() {
        List<ExtractFieldSortOrder> testSortOrder = new ArrayList<>();

        testSortOrder.add(buildSortOrder(FIELD_6, 1L));
        testSortOrder.add(buildSortOrder(FIELD_5, 2L));
        testSortOrder.add(buildSortOrder(FIELD_4, 3L));
        testSortOrder.add(buildSortOrder(FIELD_3, 4L));
        testSortOrder.add(buildSortOrder(FIELD_2, 5L));
        testSortOrder.add(buildSortOrder(FIELD_1, 6L));
        testSortOrder.add(buildSortOrder(FIELD_EXT_ONLY_3, 7L));
        testSortOrder.add(buildSortOrder(FIELD_EXT_ONLY_2, 8L));
        testSortOrder.add(buildSortOrder(FIELD_EXT_ONLY_1, 9L));
        when(extractFieldSortOrderRepository.findAllByCaseTypeOrderBySortOrder(CASE_TYPE)).thenReturn(testSortOrder);

        List<String> results = service.getAllReportingFieldsForCaseType(CASE_TYPE);

        assertThat(results.size()).isEqualTo(9);
        assertThat(results.get(0)).isEqualTo(FIELD_6);
        assertThat(results.get(1)).isEqualTo(FIELD_5);
        assertThat(results.get(2)).isEqualTo(FIELD_4);
        assertThat(results.get(3)).isEqualTo(FIELD_3);
        assertThat(results.get(4)).isEqualTo(FIELD_2);
        assertThat(results.get(5)).isEqualTo(FIELD_1);
        assertThat(results.get(6)).isEqualTo(FIELD_EXT_ONLY_3);
        assertThat(results.get(7)).isEqualTo(FIELD_EXT_ONLY_2);
        assertThat(results.get(8)).isEqualTo(FIELD_EXT_ONLY_1);

        verify(schemaService).getExtractOnlyFields();
        verify(schemaService).getAllReportingFieldsForCaseType(CASE_TYPE);
        verify(extractFieldSortOrderRepository).findAllByCaseTypeOrderBySortOrder(CASE_TYPE);
        verifyNoMoreInteractions(schemaService, extractFieldSortOrderRepository);
    }

    private void mockFields() {
        List<Field> extractOnlyFields = new ArrayList<>();
        extractOnlyFields.add(buildTestField(1L, FIELD_EXT_ONLY_1));
        extractOnlyFields.add(buildTestField(2L, FIELD_EXT_ONLY_2));
        extractOnlyFields.add(buildTestField(3L, FIELD_EXT_ONLY_3));

        List<Field> fields = new ArrayList<>();
        fields.add(buildTestField(4L, FIELD_1));
        fields.add(buildTestField(5L, FIELD_2));
        fields.add(buildTestField(6L, FIELD_3));
        fields.add(buildTestField(7L, FIELD_4));
        fields.add(buildTestField(8L, FIELD_5));
        fields.add(buildTestField(9L, FIELD_6));

        when(schemaService.getExtractOnlyFields()).thenReturn(extractOnlyFields);
        when(schemaService.getAllReportingFieldsForCaseType(CASE_TYPE)).thenReturn(fields.stream());
    }

    private Field buildTestField(Long id, String fieldName) {
        return new Field(id, null, "", fieldName, "", "", "", true, true, true, AccessLevel.READ, null);
    }

    private ExtractFieldSortOrder buildSortOrder(String fieldName, Long sortOrder) {
        return new ExtractFieldSortOrder(null, CASE_TYPE, fieldName, sortOrder);
    }
}
