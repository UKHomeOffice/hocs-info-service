package uk.gov.digital.ho.hocs.info.api.casetype;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uk.gov.digital.ho.hocs.info.api.dto.CaseTypeDto;
import uk.gov.digital.ho.hocs.info.api.dto.GetCaseTypesResponse;
import uk.gov.digital.ho.hocs.info.domain.model.CaseTypeEntity;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CaseTypeEntityResourceTest {

    @Mock
    private CaseTypeService caseTypeService;

    private CaseTypeResource caseTypeResource;


    private final String[] ROLE_SINGLE = {"DCU"};
    private final String[] ROLE_MULTI = {"DCU", "UKVI"};

    @Before
    public void setUp() {
        caseTypeResource = new CaseTypeResource(caseTypeService);
    }

    @Test
    public void shouldReturnCaseTypesWhenSingleTenantRequestedForSingleCase() {


        when(caseTypeService.getCaseTypes()).thenReturn(getMockCaseTypesSingleTenant());

        ResponseEntity<GetCaseTypesResponse> response =
                caseTypeResource.getCaseTypesSingle();

        verify(caseTypeService, times(1)).getCaseTypes();

        List<CaseTypeDto> responseEntityAsList = new ArrayList<>(Objects.requireNonNull(response.getBody()).getCaseTypes());

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        CaseTypeDto result1 = responseEntityAsList.stream().filter(x -> "MIN".equals(x.getType())).findAny().orElse(null);
        assertThat(result1).isNotNull();
        assertThat(result1.getDisplayName()).isEqualTo("DCU Ministerial");
        CaseTypeDto result2 = responseEntityAsList.stream().filter(x -> "TRO".equals(x.getType())).findAny().orElse(null);
        assertThat(result2).isNotNull();
        assertThat(result2.getDisplayName()).isEqualTo("DCU Treat Official");
        CaseTypeDto result3 = responseEntityAsList.stream().filter(x -> "DTEN".equals(x.getType())).findAny().orElse(null);
        assertThat(result3).isNotNull();
        assertThat(result3.getDisplayName()).isEqualTo("DCU Number 10");
    }

    @Test
    public void shouldReturnCaseTypesWhenSingleTenantRequestedForBulkCaseExcludingDTENCaseType() {

        when(caseTypeService.getCaseTypesBulk()).thenReturn(getMockCaseTypesSingleTenantBulk());

        ResponseEntity<GetCaseTypesResponse> response =
                caseTypeResource.getCaseTypesBulk();

        verify(caseTypeService, times(1)).getCaseTypesBulk();

        List<CaseTypeDto> responseEntityAsList = new ArrayList<>(Objects.requireNonNull(response.getBody()).getCaseTypes());

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        CaseTypeDto result1 = responseEntityAsList.stream().filter(x -> "MIN".equals(x.getType())).findAny().orElse(null);
        assertThat(result1).isNotNull();
        assertThat(result1.getDisplayName()).isEqualTo("DCU Ministerial");
        CaseTypeDto result2 = responseEntityAsList.stream().filter(x -> "TRO".equals(x.getType())).findAny().orElse(null);
        assertThat(result2).isNotNull();
        assertThat(result2.getDisplayName()).isEqualTo("DCU Treat Official");
        CaseTypeDto result3 = responseEntityAsList.stream().filter(x -> "DTEN".equals(x.getType())).findAny().orElse(null);
        assertThat(result3).isNull();
    }

    @Test
    public void shouldReturnCaseTypesWhenMultipleTenantsRequestedForSingleCase() {
        when(caseTypeService.getCaseTypes()).thenReturn(getMockCaseTypesMultipleTenant());

        ResponseEntity<GetCaseTypesResponse> response =
                caseTypeResource.getCaseTypesSingle();

        verify(caseTypeService, times(1)).getCaseTypes();

        List<CaseTypeDto> responseEntityAsList = new ArrayList<>(response.getBody().getCaseTypes());

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        CaseTypeDto result1 = responseEntityAsList.stream().filter(x -> "MIN".equals(x.getType())).findAny().orElse(null);
        assertThat(result1).isNotNull();
        assertThat(result1.getDisplayName()).isEqualTo("DCU Ministerial");
        CaseTypeDto result2 = responseEntityAsList.stream().filter(x -> "TRO".equals(x.getType())).findAny().orElse(null);
        assertThat(result2).isNotNull();
        assertThat(result2.getDisplayName()).isEqualTo("DCU Treat Official");
        CaseTypeDto result3 = responseEntityAsList.stream().filter(x -> "DTEN".equals(x.getType())).findAny().orElse(null);
        assertThat(result3).isNotNull();
        assertThat(result3.getDisplayName()).isEqualTo("DCU Number 10");
        CaseTypeDto result4 = responseEntityAsList.stream().filter(x -> "IMCB".equals(x.getType())).findAny().orElse(null);
        assertThat(result4).isNotNull();
        assertThat(result4.getDisplayName()).isEqualTo("UKVI B REF");
        CaseTypeDto result5 = responseEntityAsList.stream().filter(x -> "IMCM".equals(x.getType())).findAny().orElse(null);
        assertThat(result5).isNotNull();
        assertThat(result5.getDisplayName()).isEqualTo("UKVI Ministerial REF");
        CaseTypeDto result6 = responseEntityAsList.stream().filter(x -> "UTEN".equals(x.getType())).findAny().orElse(null);
        assertThat(result6).isNotNull();
        assertThat(result6.getDisplayName()).isEqualTo("UKVI Number 10");
    }

    @Test
    public void shouldReturnCaseTypesWhenMultipleTenantsRequestedForBulkCaseExcludingDTENCaseType() {
        when(caseTypeService.getCaseTypesBulk()).thenReturn(getMockCaseTypesMultipleTenantBulk());

        ResponseEntity<GetCaseTypesResponse> response =
                caseTypeResource.getCaseTypesBulk();

        verify(caseTypeService, times(1)).getCaseTypesBulk();

        List<CaseTypeDto> responseEntityAsList = new ArrayList<>(response.getBody().getCaseTypes());

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        CaseTypeDto result1 = responseEntityAsList.stream().filter(x -> "MIN".equals(x.getType())).findAny().orElse(null);
        assertThat(result1).isNotNull();
        assertThat(result1.getDisplayName()).isEqualTo("DCU Ministerial");
        CaseTypeDto result2 = responseEntityAsList.stream().filter(x -> "TRO".equals(x.getType())).findAny().orElse(null);
        assertThat(result2).isNotNull();
        assertThat(result2.getDisplayName()).isEqualTo("DCU Treat Official");
        CaseTypeDto result3 = responseEntityAsList.stream().filter(x -> "DTEN".equals(x.getType())).findAny().orElse(null);
        assertThat(result3).isNull();
        CaseTypeDto result4 = responseEntityAsList.stream().filter(x -> "IMCB".equals(x.getType())).findAny().orElse(null);
        assertThat(result4).isNotNull();
        assertThat(result4.getDisplayName()).isEqualTo("UKVI B REF");
        CaseTypeDto result5 = responseEntityAsList.stream().filter(x -> "IMCM".equals(x.getType())).findAny().orElse(null);
        assertThat(result5).isNotNull();
        assertThat(result5.getDisplayName()).isEqualTo("UKVI Ministerial REF");
        CaseTypeDto result6 = responseEntityAsList.stream().filter(x -> "UTEN".equals(x.getType())).findAny().orElse(null);
        assertThat(result6).isNotNull();
        assertThat(result6.getDisplayName()).isEqualTo("UKVI Number 10");
    }

    private static Set<CaseTypeEntity> getMockCaseTypesSingleTenant() {
        Set<CaseTypeEntity> caseTypesSet = new HashSet<>();
        caseTypesSet.add(new CaseTypeEntity(1L,"DCU Ministerial","a1", "MIN","DCU", "DCU_MIN_DISPATCH", true));
        caseTypesSet.add(new CaseTypeEntity(2L,"DCU Treat Official","a2", "TRO","DCU", "DCU_TRO_DISPATCH", true));
        caseTypesSet.add(new CaseTypeEntity(3L,"DCU Number 10","a3", "DTEN","DCU", "DCU_DTEN_DISPATCH", true));
        return caseTypesSet;
    }

    private static Set<CaseTypeEntity> getMockCaseTypesSingleTenantBulk() {
        Set<CaseTypeEntity> caseTypesSet = new HashSet<>();
        caseTypesSet.add(new CaseTypeEntity(1L,"DCU Ministerial", "a1", "MIN","DCU","DCU_MIN_DISPATCH",  true));
        caseTypesSet.add(new CaseTypeEntity(2L,"DCU Treat Official","a2", "TRO","DCU","DCU_TRO_DISPATCH",  true));
        return caseTypesSet;
    }

    private Set<CaseTypeEntity> getMockCaseTypesMultipleTenant() {
        Set<CaseTypeEntity> caseTypesSet = new HashSet<>();
        caseTypesSet.add(new CaseTypeEntity(1L,"DCU Ministerial","a1", "MIN","DCU","DCU_MIN_DISPATCH",  true));
        caseTypesSet.add(new CaseTypeEntity(2L,"DCU Treat Official", "a2", "TRO","DCU","DCU_TRO_DISPATCH",  true));
        caseTypesSet.add(new CaseTypeEntity(3L,"DCU Number 10","a3", "DTEN","UKVI", "DCU_DTEN_DISPATCH", true));
        caseTypesSet.add(new CaseTypeEntity(4L, "UKVI B REF", "a4", "IMCB","UKVI", "DCU_IMCB_DISPATCH", true));
        caseTypesSet.add(new CaseTypeEntity(5L, "UKVI Ministerial REF","a5", "IMCM","UKVI","DCU_IMCM_DISPATCH",  true));
        caseTypesSet.add(new CaseTypeEntity(6L, "UKVI Number 10","a6", "UTEN","UKVI", "DCU_UTEN_DISPATCH", true));
        return caseTypesSet;
    }

    private Set<CaseTypeEntity> getMockCaseTypesMultipleTenantBulk() {
        Set<CaseTypeEntity> caseTypesSet = new HashSet<>();
        caseTypesSet.add(new CaseTypeEntity(1L,"DCU Ministerial","a1", "MIN","DCU", "DCU_MIN_DISPATCH", true));
        caseTypesSet.add(new CaseTypeEntity(2L,"DCU Treat Official","a2", "TRO","DCU","DCU_TRO_DISPATCH",  true));
        caseTypesSet.add(new CaseTypeEntity(4L, "UKVI B REF","a3", "IMCB","UKVI","DCU_IMCB_DISPATCH",  true));
        caseTypesSet.add(new CaseTypeEntity(5L, "UKVI Ministerial REF","a4", "IMCM","UKVI","DCU_IMCM_DISPATCH",  true));
        caseTypesSet.add(new CaseTypeEntity(6L, "UKVI Number 10","a5", "UTEN","UKVI", "DCU_UTEN_DISPATCH", true));
        return caseTypesSet;
    }
}