package uk.gov.digital.ho.hocs.info.api;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;
import uk.gov.digital.ho.hocs.info.api.dto.ExportViewDto;
import uk.gov.digital.ho.hocs.info.api.dto.ExportViewFieldDto;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ExportViewResourceTest {

    @Mock
    private ExportViewService exportViewService;

    private ExportViewResource exportViewResource;

    private static final String PERMISSION_1 = "permission_name1";

    private static final String PERMISSION_2 = "permission_name2";

    private static final String VIEW_CODE_1 = "view_name1";

    private static final String VIEW_CODE_2 = "view_name2";

    private static final String VIEW_DISPLAY_NAME_1 = "display_name1";

    private static final String VIEW_DISPLAY_NAME_2 = "display_name2";

    private static final String FIELD_NAME_A = "FieldA";

    private static final String FIELD_NAME_B = "FieldB";

    private static final String FIELD_NAME_C = "FieldC";

    private static final String FIELD_NAME_D = "FieldD";

    @Before
    public void setUp() {
        this.exportViewResource = new ExportViewResource(exportViewService);
    }

    @Test
    public void getAllExportViews() {
        List<ExportViewDto> exportViews = buildExportViews();

        when(exportViewService.getAllExportViews()).thenReturn(exportViews);

        ResponseEntity<List<ExportViewDto>> results = exportViewResource.getAllExportViews();

        assertThat(results.getStatusCodeValue()).isEqualTo(200);
        assertThat(results.getBody()).isEqualTo(exportViews);

        verify(exportViewService).getAllExportViews();
        verifyNoMoreInteractions(exportViewService);
    }

    @Test
    public void getExportView() {
        ExportViewDto exportView = buildExportView1();

        when(exportViewService.getExportView(VIEW_CODE_1)).thenReturn(exportView);

        ResponseEntity<ExportViewDto> results = exportViewResource.getExportView(VIEW_CODE_1);

        assertThat(results.getStatusCodeValue()).isEqualTo(200);
        assertThat(results.getBody()).isEqualTo(exportView);

        verify(exportViewService).getExportView(VIEW_CODE_1);
        verifyNoMoreInteractions(exportViewService);
    }

    private List<ExportViewDto> buildExportViews() {
        return List.of(buildExportView1(), buildExportView2());
    }

    private ExportViewDto buildExportView1() {
        ExportViewFieldDto fieldA = new ExportViewFieldDto(1L, 1L, 1L, FIELD_NAME_A, null);
        ExportViewFieldDto fieldB = new ExportViewFieldDto(2L, 1L, 2L, FIELD_NAME_B, null);
        List<ExportViewFieldDto> fields1 = List.of(fieldA, fieldB);
        return new ExportViewDto(1L, VIEW_CODE_1, VIEW_DISPLAY_NAME_1, PERMISSION_1, fields1);
    }

    private ExportViewDto buildExportView2() {
        ExportViewFieldDto fieldC = new ExportViewFieldDto(3L, 2L, 1L, FIELD_NAME_C, null);
        ExportViewFieldDto fieldD = new ExportViewFieldDto(4L, 2L, 2L, FIELD_NAME_D, null);
        List<ExportViewFieldDto> fields2 = List.of(fieldC, fieldD);
        return new ExportViewDto(2L, VIEW_CODE_2, VIEW_DISPLAY_NAME_2, PERMISSION_2, fields2);
    }

}