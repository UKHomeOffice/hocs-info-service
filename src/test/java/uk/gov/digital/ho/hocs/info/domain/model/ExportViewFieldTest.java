package uk.gov.digital.ho.hocs.info.domain.model;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.digital.ho.hocs.info.api.dto.ExportViewFieldDto;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class ExportViewFieldTest {

    private static final Long ADAPTER_ID = 10L;

    private static final Long FIELD_ID = 2L;

    private static final String FIELD_DISPLAY_NAME = "FieldA";

    private static final Long VIEW_ID = 15L;

    private static final Long ADAPTER_SORT = 1L;

    private static final Long FIELD_SORT = 100L;

    private static final String ADAPTER_TYPE = "Hidden";

    private ExportViewField exportViewField;

    private ExportViewFieldAdapter exportViewFieldAdapter;

    @Before
    public void before() {
        exportViewFieldAdapter = new ExportViewFieldAdapter(ADAPTER_ID, FIELD_ID, ADAPTER_SORT, ADAPTER_TYPE);
        exportViewField = new ExportViewField(FIELD_ID, VIEW_ID, FIELD_SORT, FIELD_DISPLAY_NAME,
            List.of(exportViewFieldAdapter));
    }

    @Test
    public void getId() {
        assertThat(exportViewField.getId()).isEqualTo(FIELD_ID);
    }

    @Test
    public void getParentExportViewId() {
        assertThat(exportViewField.getParentExportViewId()).isEqualTo(VIEW_ID);
    }

    @Test
    public void getSortOrder() {
        assertThat(exportViewField.getSortOrder()).isEqualTo(FIELD_SORT);
    }

    @Test
    public void getType() {
        assertThat(exportViewField.getDisplayName()).isEqualTo(FIELD_DISPLAY_NAME);
    }

    @Test
    public void getAdapters() {
        assertThat(exportViewField.getAdapters().size()).isEqualTo(1);
        assertThat(exportViewField.getAdapters().get(0)).isEqualTo(exportViewFieldAdapter);
    }

    @Test
    public void toDto() {
        ExportViewFieldDto dto = exportViewField.toDto();

        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(FIELD_ID);
        assertThat(dto.getParentExportViewId()).isEqualTo(VIEW_ID);
        assertThat(dto.getDisplayName()).isEqualTo(FIELD_DISPLAY_NAME);
        assertThat(dto.getSortOrder()).isEqualTo(FIELD_SORT);
        assertThat(dto.getAdapters()).isNotNull();
        assertThat(dto.getAdapters().size()).isEqualTo(1);
        assertThat(dto.getAdapters().get(0).getId()).isEqualTo(ADAPTER_ID);
    }

}
