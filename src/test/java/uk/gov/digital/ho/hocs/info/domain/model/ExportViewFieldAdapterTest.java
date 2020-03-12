package uk.gov.digital.ho.hocs.info.domain.model;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.digital.ho.hocs.info.api.dto.ExportViewFieldAdapterDto;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class ExportViewFieldAdapterTest {

    private static final Long ADAPTER_ID = 10L;
    private static final Long FIELD_ID = 2L;
    private static final Long ADAPTER_SORT = 1L;
    private static final String ADAPTER_TYPE = "Hidden";

    private ExportViewFieldAdapter exportViewFieldAdapter;

    @Before
    public void before() {
        exportViewFieldAdapter = new ExportViewFieldAdapter(ADAPTER_ID, FIELD_ID, ADAPTER_SORT, ADAPTER_TYPE);
    }

    @Test
    public void getId() {
        assertThat(exportViewFieldAdapter.getId()).isEqualTo(ADAPTER_ID);
    }

    @Test
    public void getParentExportViewFieldId() {
        assertThat(exportViewFieldAdapter.getParentExportViewFieldId()).isEqualTo(FIELD_ID);
    }

    @Test
    public void getSortOrder() {
        assertThat(exportViewFieldAdapter.getSortOrder()).isEqualTo(ADAPTER_SORT);
    }

    @Test
    public void getType() {
        assertThat(exportViewFieldAdapter.getType()).isEqualTo(ADAPTER_TYPE);
    }

    @Test
    public void toDto() {
        ExportViewFieldAdapterDto dto = exportViewFieldAdapter.toDto();

        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(ADAPTER_ID);
        assertThat(dto.getParentExportViewFieldId()).isEqualTo(FIELD_ID);
        assertThat(dto.getType()).isEqualTo(ADAPTER_TYPE);
        assertThat(dto.getSortOrder()).isEqualTo(ADAPTER_SORT);
    }
}
