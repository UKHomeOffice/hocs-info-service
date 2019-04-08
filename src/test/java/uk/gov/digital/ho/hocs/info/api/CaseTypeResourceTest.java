package uk.gov.digital.ho.hocs.info.api;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uk.gov.digital.ho.hocs.info.api.dto.CaseTypeDto;
import uk.gov.digital.ho.hocs.info.api.dto.CreateCaseTypeDto;
import uk.gov.digital.ho.hocs.info.domain.model.CaseType;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CaseTypeResourceTest {

    @Mock
    private CaseTypeService caseTypeService;

    private CaseTypeResource caseTypeResource;

    @Before
    public void setUp() {
        caseTypeResource = new CaseTypeResource(caseTypeService);
    }

    @Test
    public void shouldReturnCaseTypesForSingleCase() {
        when(caseTypeService.getAllCaseTypesForUser(false)).thenReturn(getMockCaseTypes());

        ResponseEntity<Set<CaseTypeDto>> response =
                caseTypeResource.getCaseTypes(false);

        verify(caseTypeService, times(1)).getAllCaseTypesForUser(false);

        List<CaseTypeDto> responseEntityAsList = new ArrayList<>(response.getBody());

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
    public void shouldReturnCaseTypesForBulkCaseExcludingDTENCaseType() {
        when(caseTypeService.getAllCaseTypesForUser(true)).thenReturn(getMockCaseTypesBulk());

        ResponseEntity<Set<CaseTypeDto>> response =
                caseTypeResource.getCaseTypes(true);

        verify(caseTypeService, times(1)).getAllCaseTypesForUser(true);

        List<CaseTypeDto> responseEntityAsList = new ArrayList<>(response.getBody());

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

    @Test
    public void shouldAddNewCaseType() {
        CreateCaseTypeDto caseType = new CreateCaseTypeDto("New Case Type", "c1", "NEW",true,true,"STAGE_ONE");
        doNothing().when(caseTypeService).createCaseType(caseType);

        ResponseEntity response =
                caseTypeResource.createCaseType(caseType);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(caseTypeService, times(1)).createCaseType(caseType);

    }

    private Set<CaseType> getMockCaseTypes() {
        UUID unitUUID1 = UUID.randomUUID();
        UUID unitUUID2 = UUID.randomUUID();

        Set<CaseType> caseTypesSet = new HashSet<>();
        caseTypesSet.add(new CaseType(1L,UUID.randomUUID(),"DCU Ministerial","a1", "MIN",unitUUID1,"DCU_MIN_DISPATCH",  true, true));
        caseTypesSet.add(new CaseType(2L,UUID.randomUUID(),"DCU Treat Official", "a2", "TRO",unitUUID1,"DCU_TRO_DISPATCH",  true, true));
        caseTypesSet.add(new CaseType(3L,UUID.randomUUID(),"DCU Number 10","a3", "DTEN",unitUUID1, "DCU_DTEN_DISPATCH", true, true));
        caseTypesSet.add(new CaseType(4L,UUID.randomUUID(), "UKVI B REF", "a4", "IMCB",unitUUID2, "DCU_IMCB_DISPATCH", true, true));
        caseTypesSet.add(new CaseType(5L,UUID.randomUUID(), "UKVI Ministerial REF","a5", "IMCM",unitUUID2,"DCU_IMCM_DISPATCH",  true, true));
        caseTypesSet.add(new CaseType(6L,UUID.randomUUID(), "UKVI Number 10","a6", "UTEN",unitUUID2, "DCU_UTEN_DISPATCH", true, true));
        return caseTypesSet;
    }

    private Set<CaseType> getMockCaseTypesBulk() {
        UUID unitUUID1 = UUID.randomUUID();
        UUID unitUUID2 = UUID.randomUUID();

        Set<CaseType> caseTypesSet = new HashSet<>();
        caseTypesSet.add(new CaseType(1L,UUID.randomUUID(),"DCU Ministerial","a1", "MIN",unitUUID1, "DCU_MIN_DISPATCH", true, true));
        caseTypesSet.add(new CaseType(2L,UUID.randomUUID(),"DCU Treat Official","a2", "TRO",unitUUID1,"DCU_TRO_DISPATCH",  true, true));
        caseTypesSet.add(new CaseType(4L,UUID.randomUUID(), "UKVI B REF","a3", "IMCB",unitUUID2,"DCU_IMCB_DISPATCH",  true, true));
        caseTypesSet.add(new CaseType(5L,UUID.randomUUID(), "UKVI Ministerial REF","a4", "IMCM",unitUUID2,"DCU_IMCM_DISPATCH",  true, true));
        caseTypesSet.add(new CaseType(6L,UUID.randomUUID(), "UKVI Number 10","a5", "UTEN",unitUUID2, "DCU_UTEN_DISPATCH", true, true));
        return caseTypesSet;
    }
}