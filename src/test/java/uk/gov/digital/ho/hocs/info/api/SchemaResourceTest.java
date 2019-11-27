package uk.gov.digital.ho.hocs.info.api;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;
import uk.gov.digital.ho.hocs.info.api.dto.FieldDto;
import uk.gov.digital.ho.hocs.info.domain.model.Field;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SchemaResourceTest {

    @Mock
    SchemaService schemaService;

    SchemaResource service;

    @Before
    public void setup() {
        service = new SchemaResource(schemaService);
    }

    @Test
    public void shouldGetAllSummaryFieldsForCaseType() {
        Field field = new Field( "component", "Field1", "label", "", "", true);
        List<Field> fields = new ArrayList();
        fields.add(field);
        when(schemaService.getAllSummaryFieldsForCaseType("CASE_TYPE")).thenReturn(fields);

        ResponseEntity<List<FieldDto>> result = service.getAllSummaryFieldsForCaseType("CASE_TYPE");
        assertThat(result.getBody().size()).isEqualTo(1);
        assertThat(result.getBody().get(0).getName()).isEqualTo("Field1");

    }
}
