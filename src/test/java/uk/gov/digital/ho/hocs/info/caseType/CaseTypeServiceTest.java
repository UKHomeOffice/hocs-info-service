package uk.gov.digital.ho.hocs.info.caseType;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.digital.ho.hocs.info.dto.CaseTypeDto;
import uk.gov.digital.ho.hocs.info.entities.CaseType;
import uk.gov.digital.ho.hocs.info.exception.EntityNotFoundException;
import uk.gov.digital.ho.hocs.info.repositories.CaseTypeRepository;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;



@RunWith(MockitoJUnitRunner.class)
public class CaseTypeServiceTest {

    @Mock
    private CaseTypeRepository caseTypeRepository;

    private CaseTypeService caseTypeService;

    @Before
    public void setUp() {
        this.caseTypeService = new CaseTypeService(caseTypeRepository);
    }

    @Test(expected = EntityNotFoundException.class)
    public void shouldGetCaseTypes() throws EntityNotFoundException {
        caseTypeService.getCaseTypes(null);
    }

    @Test
    public void shouldGetCaseTypesSingleTenantRequested() {

        when(caseTypeRepository.findCaseTypesByTenant("DCU")).thenReturn(getDCUCaseType());

        List<CaseTypeDto> caseTypeDtos = caseTypeService.getCaseTypes(new ArrayList<String>() {{
            add("DCU");
        }});

        verify(caseTypeRepository, times(1)).findCaseTypesByTenant(any());
        assertThat(caseTypeDtos.size()).isEqualTo(3);

        CaseTypeDto result1 = caseTypeDtos.stream().filter(x -> "MIN".equals(x.getType())).findAny().orElse(null);
        assertThat(result1).isNotNull();
        assertThat(result1.getDisplayName()).isEqualTo("DCU Ministerial");
        assertThat(result1.getTenant()).isEqualTo("DCU");
        CaseTypeDto result2 = caseTypeDtos.stream().filter(x -> "TRO".equals(x.getType())).findAny().orElse(null);
        assertThat(result2).isNotNull();
        assertThat(result2.getDisplayName()).isEqualTo("DCU Treat Official");
        assertThat(result2.getTenant()).isEqualTo("DCU");
        CaseTypeDto result3 = caseTypeDtos.stream().filter(x -> "DTEN".equals(x.getType())).findAny().orElse(null);
        assertThat(result3).isNotNull();
        assertThat(result3.getDisplayName()).isEqualTo("DCU Number 10");
        assertThat(result3.getTenant()).isEqualTo("DCU");
    }

    @Test
    public void shouldGetCaseTypesMultipleTenantRequested() {

        when(caseTypeRepository.findCaseTypesByTenant("DCU")).thenReturn(getDCUCaseType());
        when(caseTypeRepository.findCaseTypesByTenant("UKVI")).thenReturn(getUKVICaseType());

        List<CaseTypeDto> caseTypeDtos = caseTypeService.getCaseTypes(new ArrayList<String>() {{
            add("DCU");
            add("UKVI");
        }});

        verify(caseTypeRepository, times(2)).findCaseTypesByTenant(any());
        assertThat(caseTypeDtos.size()).isEqualTo(6);

        CaseTypeDto result1 = caseTypeDtos.stream().filter(x -> "MIN".equals(x.getType())).findAny().orElse(null);
        assertThat(result1).isNotNull();
        assertThat(result1.getDisplayName()).isEqualTo("DCU Ministerial");
        assertThat(result1.getTenant()).isEqualTo("DCU");
        CaseTypeDto result2 = caseTypeDtos.stream().filter(x -> "TRO".equals(x.getType())).findAny().orElse(null);
        assertThat(result2).isNotNull();
        assertThat(result2.getDisplayName()).isEqualTo("DCU Treat Official");
        assertThat(result2.getTenant()).isEqualTo("DCU");
        CaseTypeDto result3 = caseTypeDtos.stream().filter(x -> "DTEN".equals(x.getType())).findAny().orElse(null);
        assertThat(result3).isNotNull();
        assertThat(result3.getDisplayName()).isEqualTo("DCU Number 10");
        assertThat(result3.getTenant()).isEqualTo("DCU");
        CaseTypeDto result4 = caseTypeDtos.stream().filter(x -> "IMCB".equals(x.getType())).findAny().orElse(null);
        assertThat(result4).isNotNull();
        assertThat(result4.getDisplayName()).isEqualTo("UKVI B REF");
        assertThat(result4.getTenant()).isEqualTo("UKVI");
        CaseTypeDto result5 = caseTypeDtos.stream().filter(x -> "IMCM".equals(x.getType())).findAny().orElse(null);
        assertThat(result5).isNotNull();
        assertThat(result5.getDisplayName()).isEqualTo("UKVI Ministerial REF");
        assertThat(result5.getTenant()).isEqualTo("UKVI");
        CaseTypeDto result6 = caseTypeDtos.stream().filter(x -> "UTEN".equals(x.getType())).findAny().orElse(null);
        assertThat(result6).isNotNull();
        assertThat(result6.getDisplayName()).isEqualTo("UKVI Number 10");
        assertThat(result6.getTenant()).isEqualTo("UKVI");
    }

    private List<CaseType> getDCUCaseType() {
        return new ArrayList<CaseType>() {{
            add(new CaseType(1, "DCU Ministerial", "MIN", new ArrayList<>()));
            add(new CaseType(2, "DCU Treat Official", "TRO", new ArrayList<>()));
            add(new CaseType(3, "DCU Number 10", "DTEN", new ArrayList<>()));
        }};
    }

    private List<CaseType> getUKVICaseType() {
        return new ArrayList<CaseType>() {{
            add(new CaseType(1, "UKVI B REF", "IMCB", new ArrayList<>()));
            add(new CaseType(2, "UKVI Ministerial REF", "IMCM", new ArrayList<>()));
            add(new CaseType(3, "UKVI Number 10", "UTEN", new ArrayList<>()));
        }};
    }
}