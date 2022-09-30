package uk.gov.digital.ho.hocs.info.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.digital.ho.hocs.info.api.dto.FieldDto;
import uk.gov.digital.ho.hocs.info.domain.exception.ApplicationExceptions;
import uk.gov.digital.ho.hocs.info.domain.model.Field;
import uk.gov.digital.ho.hocs.info.domain.model.Schema;
import uk.gov.digital.ho.hocs.info.domain.repository.FieldRepository;
import uk.gov.digital.ho.hocs.info.domain.repository.SchemaRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SchemaServiceTest {

    private final Field field = new Field("", "Field1", "", "", "", true, null);

    @Mock
    ObjectMapper mapper;

    @Mock
    private FieldRepository fieldRepository;

    @Mock
    private SchemaRepository schemaRepository;

    private SchemaService service;

    @Before
    public void setup() {
        service = new SchemaService(fieldRepository, schemaRepository, mapper);
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
    public void getSchemaByType() {
        String type = "TEST_TYPE";
        UUID uuid = UUID.randomUUID();
        Schema testSchema = new Schema(1L, uuid, type, "schemaTitle", "save", true, "stageType", null, null, null, null,
            null);

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

    @Test
    public void shouldGetAllFieldsBySchemaType() {
        List<Field> fields = new ArrayList<>() {{
            add(field);
        }};
        when(fieldRepository.findAllBySchemaType("SCHEMA_TYPE")).thenReturn(fields);

        List<FieldDto> result = service.getFieldsBySchemaType("SCHEMA_TYPE");

        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0).getName()).isEqualTo(field.getName());
    }

}
