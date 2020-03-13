package uk.gov.digital.ho.hocs.info.domain.model;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.digital.ho.hocs.info.api.dto.ExportViewDto;
import uk.gov.digital.ho.hocs.info.api.dto.ExportViewFieldDto;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class ExportViewTest {

    private static final Long ADAPTER_ID = 10L;
    private static final Long FIELD_ID = 2L;
    private static final String FIELD_DISPLAY_NAME = "FieldA";
    private static final Long VIEW_ID = 15L;
    private static final String VIEW_CODE = "view code";
    private static final String VIEW_DISPLAY_NAME = "view display name";
    private static final String VIEW_PERMISSION = "Permission 234";
    private static final Long ADAPTER_SORT = 1L;
    private static final Long FIELD_SORT = 100L;
    private static final String ADAPTER_TYPE = "Hidden";

    private ExportView exportView;
    private ExportViewField exportViewField;
    private ExportViewFieldAdapter exportViewFieldAdapter;

    @Before
    public void before() {
        exportViewFieldAdapter = new ExportViewFieldAdapter(ADAPTER_ID, FIELD_ID, ADAPTER_SORT, ADAPTER_TYPE);
        exportViewField = new ExportViewField(FIELD_ID, VIEW_ID, FIELD_SORT, FIELD_DISPLAY_NAME, List.of(exportViewFieldAdapter));
        exportView = new ExportView(VIEW_ID, VIEW_CODE, VIEW_DISPLAY_NAME, VIEW_PERMISSION, List.of(exportViewField));
    }

    @Test
    public void getId() {
        assertThat(exportView.getId()).isEqualTo(VIEW_ID);
    }

    @Test
    public void getCode() {
        assertThat(exportView.getCode()).isEqualTo(VIEW_CODE);
    }

    @Test
    public void getDisplayName() {
        assertThat(exportView.getDisplayName()).isEqualTo(VIEW_DISPLAY_NAME);
    }

    @Test
    public void getRequiredPermission() {
        assertThat(exportView.getRequiredPermission()).isEqualTo(VIEW_PERMISSION);
    }

    @Test
    public void getFields() {
        assertThat(exportView.getFields().size()).isEqualTo(1);
        assertThat(exportView.getFields().get(0)).isEqualTo(exportViewField);
    }

    @Test
    public void toDto() {
        ExportViewDto dto = exportView.toDto();

        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(VIEW_ID);
        assertThat(dto.getCode()).isEqualTo(VIEW_CODE);
        assertThat(dto.getDisplayName()).isEqualTo(VIEW_DISPLAY_NAME);
        assertThat(dto.getRequiredPermission()).isEqualTo(VIEW_PERMISSION);
        assertThat(dto.getFields()).isNotNull();
        assertThat(dto.getFields().size()).isEqualTo(1);
        assertThat(dto.getFields().get(0).getId()).isEqualTo(FIELD_ID);
    }
}
