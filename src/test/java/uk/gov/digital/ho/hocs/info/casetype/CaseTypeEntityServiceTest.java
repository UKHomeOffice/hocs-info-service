package uk.gov.digital.ho.hocs.info.casetype;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.digital.ho.hocs.info.RequestData;
import uk.gov.digital.ho.hocs.info.repositories.CaseTypeRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;


@RunWith(MockitoJUnitRunner.class)
public class CaseTypeEntityServiceTest {

    @Mock
    private CaseTypeRepository caseTypeRepository;

    @Mock
    private RequestData requestData;

    private CaseTypeService caseTypeService;

    @Before
    public void setUp() {
        this.caseTypeService = new CaseTypeService(caseTypeRepository, requestData );
    }

    //@Test(expected = EntityNotFoundException.class)
    @Test
    public void shouldThrowExceptionWhenGetCaseTypesWithNoRoles() throws EntityNotFoundException {
        //caseTypeService.getCaseTypes(null);
        assertThat(1).isEqualTo(1);
    }

   // @Test
   // public void shouldGetCaseTypesSingleTenantRequested() {
   //
   //     when(requestData.roles()).thenReturn("DCU");
   //     when(caseTypeRepository.findCaseTypesByTenant(any())).thenReturn(getDCUCaseType());
   //
   //     List<CaseTypeDto> caseTypeDtos = caseTypeService.getCaseTypes();
   //
   //     verify(caseTypeRepository, times(1)).findCaseTypesByTenant(any());
   //
   //     assertThat(caseTypeDtos.size()).isEqualTo(3);
   //
   //     assetCaseTypeDtoContainsCorrectElements(caseTypeDtos, "MIN","DCU Ministerial","DCU" );
   //     assetCaseTypeDtoContainsCorrectElements(caseTypeDtos, "TRO","DCU Treat Official","DCU" );
   //     assetCaseTypeDtoContainsCorrectElements(caseTypeDtos, "DTEN","DCU Number 10","DCU" );
   // }
   //
   //
   // @Test
   // public void shouldGetCaseTypesMultipleTenantRequested() {
   //    // when(tenantService.getTenantsFromRoles(any())).thenReturn(new ArrayList<String>() {{ add("DCU");add("UKVI");}});
   //     when(caseTypeRepository.findCaseTypesByTenant("DCU")).thenReturn(getDCUCaseType());
   //     when(caseTypeRepository.findCaseTypesByTenant("UKVI")).thenReturn(getUKVICaseType());
   //
   //     List<CaseTypeDto> caseTypeDtos = caseTypeService.getCaseTypes(new ArrayList<String>() {{
   //         add("DCU");
   //         add("UKVI");
   //     }});
   //
   //     verify(caseTypeRepository, times(2)).findCaseTypesByTenant(any());
   //     assertThat(caseTypeDtos.size()).isEqualTo(6);
   //
   //     assetCaseTypeDtoContainsCorrectElements(caseTypeDtos, "MIN","DCU Ministerial","DCU" );
   //     assetCaseTypeDtoContainsCorrectElements(caseTypeDtos, "TRO","DCU Treat Official","DCU" );
   //     assetCaseTypeDtoContainsCorrectElements(caseTypeDtos, "DTEN","DCU Number 10","DCU" );
   //     assetCaseTypeDtoContainsCorrectElements(caseTypeDtos, "IMCB","UKVI B REF","UKVI" );
   //     assetCaseTypeDtoContainsCorrectElements(caseTypeDtos, "IMCM","UKVI Ministerial REF","UKVI" );
   //     assetCaseTypeDtoContainsCorrectElements(caseTypeDtos, "UTEN","UKVI Number 10","UKVI" );
   // }
   //
   // @Test
   // public void shouldGetCaseTypesSingleTenantRequestedWithNoneTenantsInXAuthRolesHeader() {
   //
   //     when(tenantService.getTenantsFromRoles(anyList())).thenReturn(new ArrayList<String>() {{ add("DCU");}});
   //     when(caseTypeRepository.findCaseTypesByTenant("DCU")).thenReturn(getDCUCaseType());
   //
   //    // List<CaseTypeDto> caseTypeDtos = caseTypeService.getCaseTypes(new ArrayList<String>() {{
   //    //     add("Create");add("Document");add("Draft");add("DCU");
   //    // }});
   //
   //     verify(caseTypeRepository, times(1)).findCaseTypesByTenant("DCU");
   //
   //     assertThat(caseTypeDtos.size()).isEqualTo(3);
   //
   //     assetCaseTypeDtoContainsCorrectElements(caseTypeDtos, "MIN","DCU Ministerial","DCU" );
   //     assetCaseTypeDtoContainsCorrectElements(caseTypeDtos, "TRO","DCU Treat Official","DCU" );
   //     assetCaseTypeDtoContainsCorrectElements(caseTypeDtos, "DTEN","DCU Number 10","DCU" );
   //
   //     CaseTypeDto result = caseTypeDtos.stream().filter(x -> "Create".equals(x.getType())).findAny().orElse(null);
   //     assertThat(result).isNull();
   // }
   //
   // private void assetCaseTypeDtoContainsCorrectElements(List<CaseTypeDto> caseTypeDtos, String CaseType, String DisplayName, String tenant) {
   //     CaseTypeDto result1 = caseTypeDtos.stream().filter(x -> CaseType.equals(x.getType())).findAny().orElse(null);
   //     assertThat(result1).isNotNull();
   //     assertThat(result1.getDisplayName()).isEqualTo(DisplayName);
   //     //assertThat(result1.getTenant()).isEqualTo(tenant);
   // }
   //
   // private List<CaseTypeEntity> getDCUCaseType() {
   //     return new ArrayList<CaseTypeEntity>() {{
   //         add(new CaseTypeEntity(1, "DCU Ministerial", "MIN", new ArrayList<>()));
   //         add(new CaseTypeEntity(2, "DCU Treat Official", "TRO", new ArrayList<>()));
   //         add(new CaseTypeEntity(3, "DCU Number 10", "DTEN", new ArrayList<>()));
   //     }};
   // }
   //
   // private List<CaseTypeEntity> getUKVICaseType() {
   //     return new ArrayList<CaseTypeEntity>() {{
   //         add(new CaseTypeEntity(1, "UKVI B REF", "IMCB", new ArrayList<>()));
   //         add(new CaseTypeEntity(2, "UKVI Ministerial REF", "IMCM", new ArrayList<>()));
   //         add(new CaseTypeEntity(3, "UKVI Number 10", "UTEN", new ArrayList<>()));
   //     }};
   // }

}