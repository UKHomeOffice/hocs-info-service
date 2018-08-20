package uk.gov.digital.ho.hocs.info.casetype;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.digital.ho.hocs.info.RequestData;
import uk.gov.digital.ho.hocs.info.dto.CaseTypeDto;
import uk.gov.digital.ho.hocs.info.entities.CaseTypeEntity;
import uk.gov.digital.ho.hocs.info.exception.EntityNotFoundException;
import uk.gov.digital.ho.hocs.info.repositories.CaseTypeRepository;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class CaseTypeEntityServiceTest {

    @Mock
    private CaseTypeRepository caseTypeRepository;

    @Mock
    private RequestData requestData;

    private CaseTypeService caseTypeService;

    private String[] singleRole = {"DCU"};
    private String[] multiRoles = {"DCU","UKVI"};


    @Before
    public void setUp() {
        this.caseTypeService = new CaseTypeService(caseTypeRepository, requestData);
    }

    @Test
    public void shouldreturnEmptySetWhenNoRolesSet() {
        Set<CaseTypeEntity> caseTypeDtos = caseTypeService.getCaseTypes();
        verify(caseTypeRepository, times(1)).findAllCaseTypesByTenant(any());

        assertThat(caseTypeDtos.size()).isEqualTo(0);

    }

    @Test
    public void shouldGetCaseTypesSingleTenantRequested() {

        when(requestData.roles()).thenReturn(singleRole);
        when(caseTypeRepository.findAllCaseTypesByTenant(any())).thenReturn(getDCUCaseType());

        Set<CaseTypeEntity> caseTypeDtos = caseTypeService.getCaseTypes();

        verify(caseTypeRepository, times(1)).findAllCaseTypesByTenant(any());

        assertThat(caseTypeDtos.size()).isEqualTo(3);

        assetCaseTypeDtoContainsCorrectElements(caseTypeDtos, "MIN", "DCU Ministerial", "DCU");
        assetCaseTypeDtoContainsCorrectElements(caseTypeDtos, "TRO", "DCU Treat Official", "DCU");
        assetCaseTypeDtoContainsCorrectElements(caseTypeDtos, "DTEN", "DCU Number 10", "DCU");
    }


    @Test
    public void shouldGetCaseTypesMultipleTenantRequested() {
        when(requestData.roles()).thenReturn(multiRoles);
        when(caseTypeRepository.findAllCaseTypesByTenant(multiRoles)).thenReturn(getDCUAndUKVICaseType());

        Set<CaseTypeEntity> caseTypeDtos = caseTypeService.getCaseTypes();

        assertThat(caseTypeDtos.size()).isEqualTo(6);

        assetCaseTypeDtoContainsCorrectElements(caseTypeDtos, "MIN", "DCU Ministerial", "DCU");
        assetCaseTypeDtoContainsCorrectElements(caseTypeDtos, "TRO", "DCU Treat Official", "DCU");
        assetCaseTypeDtoContainsCorrectElements(caseTypeDtos, "DTEN", "DCU Number 10", "DCU");
        assetCaseTypeDtoContainsCorrectElements(caseTypeDtos, "IMCB", "UKVI B REF", "UKVI");
        assetCaseTypeDtoContainsCorrectElements(caseTypeDtos, "IMCM", "UKVI Ministerial REF", "UKVI");
        assetCaseTypeDtoContainsCorrectElements(caseTypeDtos, "UTEN", "UKVI Number 10", "UKVI");
    }


//     @Test
//     public void shouldGetCaseTypesSingleTenantRequestedWithNoneTenantsInXAuthRolesHeader() {
//
//         when(tenantService.getTenantsFromRoles(anyList())).thenReturn(new ArrayList<String>() {{ add("DCU");}});
//         when(caseTypeRepository.findCaseTypesByTenant("DCU")).thenReturn(getDCUCaseType());
//
//        // List<CaseTypeDto> caseTypeDtos = caseTypeService.getCaseTypes(new ArrayList<String>() {{
//        //     add("Create");add("Document");add("Draft");add("DCU");
//        // }});
//
//         verify(caseTypeRepository, times(1)).findCaseTypesByTenant("DCU");
//
//         assertThat(caseTypeDtos.size()).isEqualTo(3);
//
//         assetCaseTypeDtoContainsCorrectElements(caseTypeDtos, "MIN","DCU Ministerial","DCU" );
//         assetCaseTypeDtoContainsCorrectElements(caseTypeDtos, "TRO","DCU Treat Official","DCU" );
//         assetCaseTypeDtoContainsCorrectElements(caseTypeDtos, "DTEN","DCU Number 10","DCU" );
//
//         CaseTypeDto result = caseTypeDtos.stream().filter(x -> "Create".equals(x.getType())).findAny().orElse(null);
//         assertThat(result).isNull();
//     }

    private void assetCaseTypeDtoContainsCorrectElements(Set<CaseTypeEntity> caseTypeDtos, String CaseType, String DisplayName, String tenant) {
        CaseTypeEntity result1 = caseTypeDtos.stream().filter(x -> CaseType.equals(x.getType())).findAny().orElse(null);
        assertThat(result1).isNotNull();
        assertThat(result1.getDisplayName()).isEqualTo(DisplayName);
        assertThat(result1.getRole()).isEqualTo(tenant);
    }

    private Set<CaseTypeEntity> getDCUCaseType() {
        return new HashSet<>(Arrays.asList(new CaseTypeEntity(1, "DCU Ministerial", "MIN", "DCU"),
                new CaseTypeEntity(2, "DCU Treat Official", "TRO", "DCU"),
                new CaseTypeEntity(3, "DCU Number 10", "DTEN", "DCU")));
    }

    private HashSet<CaseTypeEntity> getDCUAndUKVICaseType() {
        return new HashSet<CaseTypeEntity>(Arrays.asList(
                new CaseTypeEntity(1, "DCU Ministerial", "MIN", "DCU"),
                new CaseTypeEntity(2, "DCU Treat Official", "TRO", "DCU"),
                new CaseTypeEntity(3, "DCU Number 10", "DTEN", "DCU"),
                new CaseTypeEntity(1, "UKVI B REF", "IMCB", "UKVI"),
                new CaseTypeEntity(2, "UKVI Ministerial REF", "IMCM", "UKVI"),
                new CaseTypeEntity(3, "UKVI Number 10", "UTEN", "UKVI")));
    }

}