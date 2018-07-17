package uk.gov.digital.ho.hocs.info.caseType;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.digital.ho.hocs.info.dto.CaseTypeDto;
import uk.gov.digital.ho.hocs.info.entities.CaseTypeDetail;
import uk.gov.digital.ho.hocs.info.entities.Holiday;
import uk.gov.digital.ho.hocs.info.entities.Tenant;
import uk.gov.digital.ho.hocs.info.exception.EntityNotFoundException;
import uk.gov.digital.ho.hocs.info.repositories.CaseTypeRepository;
import uk.gov.digital.ho.hocs.info.repositories.TenantRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class CaseTypeDetailServiceTest {

    @Mock
    private CaseTypeRepository caseTypeRepository;
    @Mock
    private TenantRepository tenantRepository;

    private CaseTypeService caseTypeService;

    @Before
    public void setUp() {
        this.caseTypeService = new CaseTypeService(caseTypeRepository, tenantRepository);
    }

    @Test(expected = EntityNotFoundException.class)
    public void shouldGetCaseTypes() throws EntityNotFoundException {
        caseTypeService.getCaseTypes(null);
    }

    @Test
    public void shouldGetCaseTypesSingleTenantRequested() {

        when(tenantRepository.findAll()).thenReturn(getTenants());
        when(caseTypeRepository.findCaseTypesByTenant("DCU")).thenReturn(getDCUCaseType());

        List<CaseTypeDto> caseTypeDtos = caseTypeService.getCaseTypes(new ArrayList<String>() {{
            add("DCU");
        }});

        verify(caseTypeRepository, times(1)).findCaseTypesByTenant(any());

        assertThat(caseTypeDtos.size()).isEqualTo(3);

        assetCaseTypeDtoContainsCorrectElements(caseTypeDtos, "MIN","DCU Ministerial","DCU" );
        assetCaseTypeDtoContainsCorrectElements(caseTypeDtos, "TRO","DCU Treat Official","DCU" );
        assetCaseTypeDtoContainsCorrectElements(caseTypeDtos, "DTEN","DCU Number 10","DCU" );
    }


    @Test
    public void shouldGetCaseTypesMultipleTenantRequested() {
        when(tenantRepository.findAll()).thenReturn(getTenants());
        when(caseTypeRepository.findCaseTypesByTenant("DCU")).thenReturn(getDCUCaseType());
        when(caseTypeRepository.findCaseTypesByTenant("UKVI")).thenReturn(getUKVICaseType());

        List<CaseTypeDto> caseTypeDtos = caseTypeService.getCaseTypes(new ArrayList<String>() {{
            add("DCU");
            add("UKVI");
        }});

        verify(caseTypeRepository, times(2)).findCaseTypesByTenant(any());
        assertThat(caseTypeDtos.size()).isEqualTo(6);

        assetCaseTypeDtoContainsCorrectElements(caseTypeDtos, "MIN","DCU Ministerial","DCU" );
        assetCaseTypeDtoContainsCorrectElements(caseTypeDtos, "TRO","DCU Treat Official","DCU" );
        assetCaseTypeDtoContainsCorrectElements(caseTypeDtos, "DTEN","DCU Number 10","DCU" );
        assetCaseTypeDtoContainsCorrectElements(caseTypeDtos, "IMCB","UKVI B REF","UKVI" );
        assetCaseTypeDtoContainsCorrectElements(caseTypeDtos, "IMCM","UKVI Ministerial REF","UKVI" );
        assetCaseTypeDtoContainsCorrectElements(caseTypeDtos, "UTEN","UKVI Number 10","UKVI" );
    }

    @Test
    public void shouldGetCaseTypesSingleTenantRequestedWithNoneTenantsInXAuthRollsHeader() {

        when(tenantRepository.findAll()).thenReturn(getTenants());
        when(caseTypeRepository.findCaseTypesByTenant("DCU")).thenReturn(getDCUCaseType());

        List<CaseTypeDto> caseTypeDtos = caseTypeService.getCaseTypes(new ArrayList<String>() {{
            add("Create");add("Document");add("Draft");add("DCU");
        }});

        verify(caseTypeRepository, times(1)).findCaseTypesByTenant(any());

        assertThat(caseTypeDtos.size()).isEqualTo(3);

        assetCaseTypeDtoContainsCorrectElements(caseTypeDtos, "MIN","DCU Ministerial","DCU" );
        assetCaseTypeDtoContainsCorrectElements(caseTypeDtos, "TRO","DCU Treat Official","DCU" );
        assetCaseTypeDtoContainsCorrectElements(caseTypeDtos, "DTEN","DCU Number 10","DCU" );

        CaseTypeDto result = caseTypeDtos.stream().filter(x -> "Create".equals(x.getType())).findAny().orElse(null);
        assertThat(result).isNull();
    }

    private void assetCaseTypeDtoContainsCorrectElements(List<CaseTypeDto> caseTypeDtos, String CaseType, String DisplayName, String tenant) {
        CaseTypeDto result1 = caseTypeDtos.stream().filter(x -> CaseType.equals(x.getType())).findAny().orElse(null);
        assertThat(result1).isNotNull();
        assertThat(result1.getDisplayName()).isEqualTo(DisplayName);
        assertThat(result1.getTenant()).isEqualTo(tenant);
    }

    private List<CaseTypeDetail> getDCUCaseType() {
        return new ArrayList<CaseTypeDetail>() {{
            add(new CaseTypeDetail(1, "DCU Ministerial", "MIN", new ArrayList<>()));
            add(new CaseTypeDetail(2, "DCU Treat Official", "TRO", new ArrayList<>()));
            add(new CaseTypeDetail(3, "DCU Number 10", "DTEN", new ArrayList<>()));
        }};
    }

    private List<CaseTypeDetail> getUKVICaseType() {
        return new ArrayList<CaseTypeDetail>() {{
            add(new CaseTypeDetail(1, "UKVI B REF", "IMCB", new ArrayList<>()));
            add(new CaseTypeDetail(2, "UKVI Ministerial REF", "IMCM", new ArrayList<>()));
            add(new CaseTypeDetail(3, "UKVI Number 10", "UTEN", new ArrayList<>()));
        }};
    }
    private List<Tenant> getTenants() {
        return new ArrayList<Tenant>() {{
            add(new Tenant(1, "RSH", new HashSet<>(), new ArrayList<>()));
            add(new Tenant(2, "DCU", new HashSet<>(), new ArrayList<>()));
            add(new Tenant(3, "UKVI", new HashSet<>(), new ArrayList<>()));
            add(new Tenant(4, "FOI", new HashSet<>(), new ArrayList<>()));
            add(new Tenant(5, "HMPOCOR", new HashSet<>(), new ArrayList<>()));
            add(new Tenant(6, "HMPOCOL", new HashSet<>(), new ArrayList<>()));
        }};
    }
}