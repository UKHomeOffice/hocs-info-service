package uk.gov.digital.ho.hocs.info.api;

import com.fasterxml.jackson.databind.ObjectMapper;
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

    @Mock
    ObjectMapper mapper;

    private SchemaResource schemaResource;

    @Before
    public void setup() {
        schemaResource = new SchemaResource(schemaService);
    }

    @Test
    public void shouldGetAllSummaryFieldsForCaseType() {
        Field childField = new Field("component", "childField", "label", "", "", true, null);
        Field field = new Field("component", "Field1", "label", "", "", true, childField);
        List<Field> fields = new ArrayList<>();
        fields.add(field);
        when(schemaService.getAllSummaryFieldsForCaseType("CASE_TYPE")).thenReturn(fields);

        ResponseEntity<List<FieldDto>> result = schemaResource.getAllSummaryFieldsForCaseType("CASE_TYPE");
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().size()).isEqualTo(1);
        assertThat(result.getBody().get(0).getName()).isEqualTo("Field1");
        assertThat(result.getBody().get(0).getChild().getUuid()).isEqualTo(childField.getUuid());

    }

    @Test
    public void shouldGetAllFieldsBySchemaType() {
        Field childField = new Field("component", "childField", "label", "", "", true, null);
        Field field = new Field("component", "Field1", "label", "", "", true, childField);

        FieldDto fieldDto = FieldDto.fromWithDecoratedProps(field, mapper);
        List<FieldDto> fieldDtos = new ArrayList<>();
        fieldDtos.add(fieldDto);

        when(schemaService.getFieldsBySchemaType("SCHEMA_TYPE")).thenReturn(fieldDtos);

        ResponseEntity<List<FieldDto>> result = schemaResource.getFieldsBySchemaType("SCHEMA_TYPE");
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().size()).isEqualTo(1);
        assertThat(result.getBody().get(0).getName()).isEqualTo("Field1");
        assertThat(result.getBody().get(0).getChild().getUuid()).isEqualTo(childField.getUuid());
    }

}
