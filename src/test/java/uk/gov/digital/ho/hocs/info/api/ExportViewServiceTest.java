package uk.gov.digital.ho.hocs.info.api;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.digital.ho.hocs.info.api.dto.ExportViewDto;
import uk.gov.digital.ho.hocs.info.domain.exception.ApplicationExceptions;
import uk.gov.digital.ho.hocs.info.domain.model.ExportView;
import uk.gov.digital.ho.hocs.info.domain.model.ExportViewField;
import uk.gov.digital.ho.hocs.info.domain.repository.ExportViewRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ExportViewServiceTest {

    @Mock
    private ExportViewRepository exportViewRepository;

    private ExportViewService exportViewService;

    private static final UUID USER_UUID = UUID.randomUUID();
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
        this.exportViewService = new ExportViewService(exportViewRepository);
    }

    @Test
    public void getAllExportViews() {

        List<ExportView> exportViews = buildExportViews();

        when(exportViewRepository.findAll()).thenReturn(exportViews);

        List<ExportViewDto> results = exportViewService.getAllExportViews();

        assertThat(results.size()).isEqualTo(2);
        assertThat(results.get(0).getCode()).isEqualTo(VIEW_CODE_1);
        assertThat(results.get(1).getCode()).isEqualTo(VIEW_CODE_2);
        assertThat(results.get(0).getDisplayName()).isEqualTo(VIEW_DISPLAY_NAME_1);
        assertThat(results.get(1).getDisplayName()).isEqualTo(VIEW_DISPLAY_NAME_2);
        assertThat(results.get(0).getId()).isEqualTo(1L);
        assertThat(results.get(1).getId()).isEqualTo(2L);
        assertThat(results.get(0).getRequiredPermission()).isEqualTo(PERMISSION_1);
        assertThat(results.get(1).getRequiredPermission()).isEqualTo(PERMISSION_2);
        assertThat(results.get(0).getFields().size()).isEqualTo(2);
        assertThat(results.get(0).getFields().get(0).getId()).isEqualTo(1);
        assertThat(results.get(0).getFields().get(0).getDisplayName()).isEqualTo(FIELD_NAME_A);
        assertThat(results.get(0).getFields().get(1).getId()).isEqualTo(2);
        assertThat(results.get(0).getFields().get(1).getDisplayName()).isEqualTo(FIELD_NAME_B);
        assertThat(results.get(1).getFields().size()).isEqualTo(2);
        assertThat(results.get(1).getFields().get(0).getId()).isEqualTo(3);
        assertThat(results.get(1).getFields().get(0).getDisplayName()).isEqualTo(FIELD_NAME_C);
        assertThat(results.get(1).getFields().get(1).getId()).isEqualTo(4);
        assertThat(results.get(1).getFields().get(1).getDisplayName()).isEqualTo(FIELD_NAME_D);

        verify(exportViewRepository).findAll();
        verifyNoMoreInteractions(exportViewRepository);
    }


    @Test
    public void getAllExportViews_Blank() {

        List<ExportView> exportViews = new ArrayList<>();

        when(exportViewRepository.findAll()).thenReturn(exportViews);

        List<ExportViewDto> results = exportViewService.getAllExportViews();

        assertThat(results.size()).isEqualTo(0);
        verify(exportViewRepository).findAll();
        verifyNoMoreInteractions(exportViewRepository);
    }


    @Test
    public void getExportView() {

        when(exportViewRepository.findByCode(VIEW_CODE_1)).thenReturn(buildExportView1());

        ExportViewDto result = exportViewService.getExportView(VIEW_CODE_1);

        assertThat(result).isNotNull();
        assertThat(result.getCode()).isEqualTo(VIEW_CODE_1);
        assertThat(result.getDisplayName()).isEqualTo(VIEW_DISPLAY_NAME_1);
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getRequiredPermission()).isEqualTo(PERMISSION_1);
        assertThat(result.getFields().size()).isEqualTo(2);
        assertThat(result.getFields().get(0).getId()).isEqualTo(1);
        assertThat(result.getFields().get(0).getDisplayName()).isEqualTo(FIELD_NAME_A);
        assertThat(result.getFields().get(1).getId()).isEqualTo(2);
        assertThat(result.getFields().get(1).getDisplayName()).isEqualTo(FIELD_NAME_B);

        verify(exportViewRepository).findByCode(VIEW_CODE_1);
        verifyNoMoreInteractions(exportViewRepository);
    }

    @Test(expected = ApplicationExceptions.EntityNotFoundException.class)
    public void getExportView_NotFound() {
        when(exportViewRepository.findByCode(VIEW_CODE_1)).thenReturn(null);

        exportViewService.getExportView(VIEW_CODE_1);

    }


    private List<ExportView> buildExportViews() {
        return List.of(buildExportView1(), buildExportView2());
    }

    private ExportView buildExportView1() {
        ExportViewField fieldA = new ExportViewField(1L, 1L, 1L, FIELD_NAME_A, null);
        ExportViewField fieldB = new ExportViewField(2L, 1L, 2L, FIELD_NAME_B, null);
        List<ExportViewField> fields1 = List.of(fieldA, fieldB);
        return new ExportView(1L, VIEW_CODE_1, VIEW_DISPLAY_NAME_1, PERMISSION_1, fields1);
    }

    private ExportView buildExportView2() {
        ExportViewField fieldC = new ExportViewField(3L, 2L, 1L, FIELD_NAME_C, null);
        ExportViewField fieldD = new ExportViewField(4L, 2L, 2L, FIELD_NAME_D, null);
        List<ExportViewField> fields2 = List.of(fieldC, fieldD);
        return new ExportView(2L, VIEW_CODE_2, VIEW_DISPLAY_NAME_2, PERMISSION_2, fields2);
    }


}