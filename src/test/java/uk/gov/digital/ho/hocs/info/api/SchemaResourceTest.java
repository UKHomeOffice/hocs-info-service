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
import uk.gov.digital.ho.hocs.info.domain.repository.FieldRepository;
import uk.gov.digital.ho.hocs.info.security.AccessLevel;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SchemaResourceTest {

    @Mock
    SchemaService schemaService;
    @Mock
    ExtractService extractService;
    @Mock
    ObjectMapper mapper;

    @Mock
    FieldRepository fieldRepository;

    private SchemaResource schemaResource;

    @Before
    public void setup() {
        schemaResource = new SchemaResource(schemaService, extractService);
    }

    @Test
    public void shouldGetAllSummaryFieldsForCaseType() {
        Field childField = new Field("component", "childField", "label", "", "", true, AccessLevel.READ, null);
        Field field = new Field("component", "Field1", "label", "", "", true, AccessLevel.READ, childField);
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
    public void getAllReportingFieldsForCaseType() {
        String caseType = "Type1";

        when(extractService.getAllReportingFieldsForCaseType(caseType)).thenReturn(List.of("Field4ExtractOnly", "Field1", "Field2", "Field3"));

        ResponseEntity<List<String>> result = schemaResource.getAllReportingFieldsForCaseType(caseType);

        assertThat(result).isNotNull();
        assertThat(result.getStatusCodeValue()).isEqualTo(200);
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().size()).isEqualTo(4);
        List<String> resultList = new ArrayList<>(result.getBody());
        assertThat(resultList.get(0)).isEqualTo("Field4ExtractOnly");
        assertThat(resultList.get(1)).isEqualTo("Field1");
        assertThat(resultList.get(2)).isEqualTo("Field2");
        assertThat(resultList.get(3)).isEqualTo("Field3");


        verify(extractService).getAllReportingFieldsForCaseType(caseType);

        verifyNoMoreInteractions(schemaService);

    }

    @Test
    public void shouldGetAllFieldsBySchemaType() {
        Field childField = new Field("component", "childField", "label", "", "", true, AccessLevel.READ, null);
        Field field = new Field("component", "Field1", "label", "", "", true, AccessLevel.READ, childField);

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

    @Test
    public void shouldGetFieldsByPermissionLevel() {
        // Given
        AccessLevel accessLevel = AccessLevel.RESTRICTED_OWNER;
        Field field = new Field("component", "Field1", "label", "", "", true, AccessLevel.RESTRICTED_OWNER, null);
        List<Field> fieldList = List.of(field);
        List<FieldDto> fieldDtos = fieldList.stream().map(FieldDto::from).collect(Collectors.toList());

        when(schemaService.getFieldsByPermissionLevel(eq(accessLevel))).thenReturn(fieldDtos);

        // WHEN
        ResponseEntity<List<FieldDto>> result = schemaResource.getFieldsByPermissionLevel(accessLevel.toString());

        // THEN
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().size()).isEqualTo(1);
        assertThat(result.getBody().get(0).getAccessLevel().getLevel()).isEqualTo(AccessLevel.RESTRICTED_OWNER.getLevel());
    }
}
