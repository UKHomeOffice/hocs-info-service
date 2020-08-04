package uk.gov.digital.ho.hocs.info.api;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.digital.ho.hocs.info.domain.exception.ApplicationExceptions;
import uk.gov.digital.ho.hocs.info.domain.model.Field;
import uk.gov.digital.ho.hocs.info.domain.model.FieldScreen;
import uk.gov.digital.ho.hocs.info.domain.model.Schema;
import uk.gov.digital.ho.hocs.info.domain.repository.FieldRepository;
import uk.gov.digital.ho.hocs.info.domain.repository.SchemaRepository;

import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SchemaServiceTest {

    @Mock
    private FieldRepository fieldRepository;

    @Mock
    private SchemaRepository schemaRepository;

    private SchemaService service;
    private final Field field = new Field("", "Field1", "", "", "", true);

    @Before
    public void setup() {
        service = new SchemaService(fieldRepository, schemaRepository);
    }

    @Test
    public void shouldGetAllStageTypes() {
        List<Field> fields = new ArrayList<>() {{
            add(field);
        }};
        when(fieldRepository.findAllSummaryFields("CASE_TYPE")).thenReturn(fields);

        List<Field> result = service.getAllSummaryFieldsForCaseType("CASE_TYPE");

        assertThat(result.size()).isEqualTo(1);
        assertThat(result.toArray()[0]).isEqualTo(field);
    }

    @Test
    public void getExtractOnlyFields() {

        Field field1 = new Field(10L, UUID.randomUUID(), "component", "Field1", "label", "", "", false, false, true);
        Field field2 = new Field(11L, UUID.randomUUID(), "component", "Field2", "label", "", "", false, true, true);
        UUID schemaUUID = UUID.randomUUID();


        List<FieldScreen> fields = List.of(
                new FieldScreen(schemaUUID, field1.getUuid(), 1L, field1),
                new FieldScreen(schemaUUID, field2.getUuid(), 2L, field2));


        Schema testSchema = new Schema(20L, schemaUUID, "type", "schemaTitle", "save", true, "stageType", fields, null, null);

        when(schemaRepository.findExtractOnlySchema()).thenReturn(testSchema);

        List<Field> results = service.getExtractOnlyFields();

        assertThat(results.size()).isEqualTo(1);
        assertThat(results.get(0).getId()).isEqualTo(11L);

        verify(schemaRepository).findExtractOnlySchema();

        verifyNoMoreInteractions(fieldRepository, schemaRepository);

    }

    @Test
    public void getExtractOnlyFields_NullSchema() {

        when(schemaRepository.findExtractOnlySchema()).thenReturn(null);

        List<Field> results = service.getExtractOnlyFields();

        assertThat(results).isNotNull();
        assertThat(results.size()).isEqualTo(0);

        verify(schemaRepository).findExtractOnlySchema();

        verifyNoMoreInteractions(fieldRepository, schemaRepository);

    }

    @Test
    public void getAllReportingFieldsForCaseType() {
        String caseType = "TYPE1";

        Field field1 = new Field(10L, UUID.randomUUID(), "component", "Field1", "label", "", "", false, false, true);
        Field field2 = new Field(11L, UUID.randomUUID(), "component", "Field2", "label", "", "", false, true, true);
        Field field3 = new Field(12L, UUID.randomUUID(), "component", "Field3", "label", "", "", false, true, true);
        Field field4 = new Field(13L, UUID.randomUUID(), "component", "Field4", "label", "", "", false, false, true);

        UUID schema1UUID = UUID.randomUUID();
        UUID schema2UUID = UUID.randomUUID();

        List<FieldScreen> fields1 = List.of(
                new FieldScreen(schema1UUID, field1.getUuid(), 1L, field1),
                new FieldScreen(schema1UUID, field2.getUuid(), 2L, field2));
        List<FieldScreen> fields2 = List.of(
                new FieldScreen(schema2UUID, field3.getUuid(), 1L, field3),
                new FieldScreen(schema2UUID, field4.getUuid(), 2L, field4));

        Schema schema1 = new Schema(21L, UUID.randomUUID(), "type1", "schemaTitle1", "save", true, "stageType", fields1, null, null);
        Schema schema2 = new Schema(22L, UUID.randomUUID(), "type2", "schemaTitle2", "save", true, "stageType", fields2, null, null);

        when(schemaRepository.findAllActiveFormsByCaseType(caseType)).thenReturn(Set.of(schema1, schema2));

        List<Field> results = service.getAllReportingFieldsForCaseType(caseType).collect(Collectors.toList());

        assertThat(results).isNotNull();
        assertThat(results.size()).isEqualTo(2);

        if (results.get(0).getId().equals(11L)) {
            assertThat(results.get(0).getId()).isEqualTo(11L);
            assertThat(results.get(0).getName()).isEqualTo("Field2");
            assertThat(results.get(1).getId()).isEqualTo(12L);
            assertThat(results.get(1).getName()).isEqualTo("Field3");

        } else {
            assertThat(results.get(0).getId()).isEqualTo(12L);
            assertThat(results.get(0).getName()).isEqualTo("Field3");
            assertThat(results.get(1).getId()).isEqualTo(11L);
            assertThat(results.get(1).getName()).isEqualTo("Field2");
        }

        verify(schemaRepository).findAllActiveFormsByCaseType(caseType);

        verifyNoMoreInteractions(fieldRepository, schemaRepository);
    }

    @Test
    public void getSchemaByType() {
        String type = "TEST_TYPE";
        UUID uuid = UUID.randomUUID();
        Schema testSchema = new Schema(1L, uuid, type, "schemaTitle", "save", true, "stageType", null, null, null);

        when(schemaRepository.findByType(type)).thenReturn(testSchema);

        Schema result = service.getSchemaByType(type);

        assertThat(result).isNotNull();
        assertThat(result.getUuid()).isEqualTo(uuid);
        assertThat(result.getType()).isEqualTo(type);
        assertThat(result.getTitle()).isEqualTo("schemaTitle");
        assertThat(result.getActionLabel()).isEqualTo("save");
        assertThat(result.isActive()).isEqualTo(true);
        assertThat(result.getStageType()).isEqualTo("stageType");

        verify(schemaRepository).findByType(type);

        verifyNoMoreInteractions(fieldRepository, schemaRepository);
    }

    @Test(expected = ApplicationExceptions.EntityNotFoundException.class)
    public void getSchemaByType_NullResult() {
        String type = "TEST_TYPE";

        when(schemaRepository.findByType(type)).thenReturn(null);

        service.getSchemaByType(type);
    }

    @Test
    public void getAllSchemasForCaseTypeAndStage() {

        String stages = "STAGE_1,STAGE_2";
        String caseType = "caseType";

        List<String> expectedStagesList = new ArrayList<>();
        expectedStagesList.add("STAGE_1");
        expectedStagesList.add("STAGE_2");

        service.getAllSchemasForCaseTypeAndStage(caseType, stages);
        verify(schemaRepository).findAllActiveFormsByCaseTypeAndStages(caseType, expectedStagesList);
    }

}
