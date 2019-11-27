package uk.gov.digital.ho.hocs.info.api;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.digital.ho.hocs.info.domain.model.Field;
import uk.gov.digital.ho.hocs.info.domain.repository.FieldRepository;
import uk.gov.digital.ho.hocs.info.domain.repository.SchemaRepository;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SchemaServiceTest {

    @Mock
    private FieldRepository fieldRepository;

    @Mock
    private SchemaRepository schemaRepository;

    private SchemaService service;
    private final Field field = new Field( "", "Field1", "", "", "", true);

    @Before
    public void setup() {
        service = new SchemaService(fieldRepository, schemaRepository);
    }

    @Test
    public void shouldGetAllStageTypes() {
        List<Field> fields = new ArrayList<Field>() {{
            add(field);
        }};
        when(fieldRepository.findAllSummaryFields("CASE_TYPE")).thenReturn(fields);

        List<Field> result = service.getAllSummaryFieldsForCaseType("CASE_TYPE");

        assertThat(result.size()).isEqualTo(1);
        assertThat(result.toArray()[0]).isEqualTo(field);
    }
}
