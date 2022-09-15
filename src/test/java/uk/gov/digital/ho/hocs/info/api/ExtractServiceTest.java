package uk.gov.digital.ho.hocs.info.api;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.digital.ho.hocs.info.domain.model.Field;
import uk.gov.digital.ho.hocs.info.security.AccessLevel;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ExtractServiceTest {

    @Mock
    private SchemaService schemaService;

    private ExtractService service;

    private static final String CASE_TYPE = "CASE_TYPE";

    private static final String FIELD_1 = "Field1";

    private static final String FIELD_2 = "Field2";

    private static final String FIELD_3 = "Field3";

    private static final String FIELD_4 = "Field4";

    private static final String FIELD_5 = "Field5";

    private static final String FIELD_6 = "Field6";

    @Before
    public void setup() {
        service = new ExtractService(schemaService);
        mockFields();
    }

    @Test
    public void getAllReportingFieldsForCaseType() {
        List<String> results = service.getAllReportingFieldsForCaseType(CASE_TYPE);

        assertThat(results.size()).isEqualTo(6);
        assertThat(results.get(0)).isEqualTo(FIELD_1);
        assertThat(results.get(1)).isEqualTo(FIELD_2);
        assertThat(results.get(2)).isEqualTo(FIELD_3);
        assertThat(results.get(3)).isEqualTo(FIELD_4);
        assertThat(results.get(4)).isEqualTo(FIELD_5);
        assertThat(results.get(5)).isEqualTo(FIELD_6);

        verify(schemaService).getExtractOnlyFields();
        verify(schemaService).getAllReportingFieldsForCaseType(CASE_TYPE);
    }

    private void mockFields() {
        List<Field> fields = List.of(buildTestField(1L, FIELD_1), buildTestField(3L, FIELD_3),
            buildTestField(2L, FIELD_2), buildTestField(5L, FIELD_5), buildTestField(4L, FIELD_4),
            buildTestField(6L, FIELD_6));

        when(schemaService.getExtractOnlyFields()).thenReturn(Collections.emptyList());
        when(schemaService.getAllReportingFieldsForCaseType(CASE_TYPE)).thenReturn(fields.stream());
    }

    private Field buildTestField(Long id, String fieldName) {
        return new Field(id, null, "", fieldName, "", "", "", true, true, true, AccessLevel.READ, null);
    }

}
