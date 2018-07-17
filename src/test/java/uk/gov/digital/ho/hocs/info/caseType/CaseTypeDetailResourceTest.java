package uk.gov.digital.ho.hocs.info.caseType;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uk.gov.digital.ho.hocs.info.dto.CaseTypeDto;
import uk.gov.digital.ho.hocs.info.dto.GetCaseTypesResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CaseTypeDetailResourceTest {

    @Mock
    private CaseTypeService caseTypeService;

    private CaseTypeResource caseTypeResource;


    private final String[] CASE_TYPE_SINGLE = {"DCU"};
    private final String[] CASE_TYPE_MULTI = {"DCU","UKVI"};

    @Before
    public void setUp() {
        caseTypeResource = new CaseTypeResource(caseTypeService);
    }

    @Test
    @SuppressFBWarnings("NP_NULL_ON_SOME_PATH_FROM_RETURN_VALUE")
    public void shouldReturnCaseTypesWhenSingleTenantRequested() {

        when(caseTypeService.getCaseTypes(any())).thenReturn(getMockCaseTypesSingleTenant());

        ResponseEntity<GetCaseTypesResponse> response =
                caseTypeResource.getAllCaseTypes(CASE_TYPE_SINGLE);

        verify(caseTypeService, times(1)).getCaseTypes(new ArrayList<String>() {{
            add("DCU");
        }});

        List<CaseTypeDto> responseEntityAsList = new ArrayList<>(Objects.requireNonNull(response.getBody()).getCaseTypes());

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        CaseTypeDto result1 = responseEntityAsList.stream().filter(x -> "MIN".equals(x.getType())).findAny().orElse(null);
        assertThat(result1).isNotNull();
        assertThat(result1.getDisplayName()).isEqualTo("DCU Ministerial");
        assertThat(result1.getTenant()).isEqualTo("DCU");
        CaseTypeDto result2 = responseEntityAsList.stream().filter(x -> "TRO".equals(x.getType())).findAny().orElse(null);
        assertThat(result2).isNotNull();
        assertThat(result2.getDisplayName()).isEqualTo("DCU Treat Official");
        assertThat(result2.getTenant()).isEqualTo("DCU");
        CaseTypeDto result3 = responseEntityAsList.stream().filter(x -> "DTEN".equals(x.getType())).findAny().orElse(null);
        assertThat(result3).isNotNull();
        assertThat(result3.getDisplayName()).isEqualTo("DCU Number 10");
        assertThat(result3.getTenant()).isEqualTo("DCU");
    }

    @Test
    @SuppressFBWarnings("NP_NULL_ON_SOME_PATH_FROM_RETURN_VALUE")
    public void shouldReturnCaseTypesWhenMultipleTenantsRequested() {

        when(caseTypeService.getCaseTypes(any())).thenReturn(getMockCaseTypesMultipleTenant());

        ResponseEntity<GetCaseTypesResponse> response =
                caseTypeResource.getAllCaseTypes(CASE_TYPE_MULTI);

        verify(caseTypeService, times(1)).getCaseTypes(new ArrayList<String>() {{
            add("DCU");
            add("UKVI");
        }});

        List<CaseTypeDto> responseEntityAsList = new ArrayList<>(response.getBody().getCaseTypes());

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        CaseTypeDto result1 = responseEntityAsList.stream().filter(x -> "MIN".equals(x.getType())).findAny().orElse(null);
        assertThat(result1).isNotNull();
        assertThat(result1.getDisplayName()).isEqualTo("DCU Ministerial");
        assertThat(result1.getTenant()).isEqualTo("DCU");
        CaseTypeDto result2 = responseEntityAsList.stream().filter(x -> "TRO".equals(x.getType())).findAny().orElse(null);
        assertThat(result2).isNotNull();
        assertThat(result2.getDisplayName()).isEqualTo("DCU Treat Official");
        assertThat(result2.getTenant()).isEqualTo("DCU");
        CaseTypeDto result3 = responseEntityAsList.stream().filter(x -> "DTEN".equals(x.getType())).findAny().orElse(null);
        assertThat(result3).isNotNull();
        assertThat(result3.getDisplayName()).isEqualTo("DCU Number 10");
        assertThat(result3.getTenant()).isEqualTo("DCU");
        CaseTypeDto result4 = responseEntityAsList.stream().filter(x -> "IMCB".equals(x.getType())).findAny().orElse(null);
        assertThat(result4).isNotNull();
        assertThat(result4.getDisplayName()).isEqualTo("UKVI B REF");
        assertThat(result4.getTenant()).isEqualTo("UKVI");
        CaseTypeDto result5 = responseEntityAsList.stream().filter(x -> "IMCM".equals(x.getType())).findAny().orElse(null);
        assertThat(result5).isNotNull();
        assertThat(result5.getDisplayName()).isEqualTo("UKVI Ministerial REF");
        assertThat(result5.getTenant()).isEqualTo("UKVI");
        CaseTypeDto result6 = responseEntityAsList.stream().filter(x -> "UTEN".equals(x.getType())).findAny().orElse(null);
        assertThat(result6).isNotNull();
        assertThat(result6.getDisplayName()).isEqualTo("UKVI Number 10");
        assertThat(result6.getTenant()).isEqualTo("UKVI");
    }

    private List<CaseTypeDto> getMockCaseTypesSingleTenant() {
        return new ArrayList<CaseTypeDto>() {{
            add(new CaseTypeDto("DCU", "DCU Ministerial", "MIN"));
            add(new CaseTypeDto("DCU", "DCU Treat Official", "TRO"));
            add(new CaseTypeDto("DCU", "DCU Number 10", "DTEN"));
        }};
    }

    private List<CaseTypeDto> getMockCaseTypesMultipleTenant() {
        return new ArrayList<CaseTypeDto>() {{
            add(new CaseTypeDto("DCU", "DCU Ministerial", "MIN"));
            add(new CaseTypeDto("DCU", "DCU Treat Official", "TRO"));
            add(new CaseTypeDto("DCU", "DCU Number 10", "DTEN"));
            add(new CaseTypeDto("UKVI", "UKVI B REF", "IMCB"));
            add(new CaseTypeDto("UKVI", "UKVI Ministerial REF", "IMCM"));
            add(new CaseTypeDto("UKVI", "UKVI Number 10", "UTEN"));
        }};

    }
}