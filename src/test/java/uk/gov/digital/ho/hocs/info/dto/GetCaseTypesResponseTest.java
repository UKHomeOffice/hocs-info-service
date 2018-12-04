package uk.gov.digital.ho.hocs.info.dto;

import org.junit.Test;
import uk.gov.digital.ho.hocs.info.entities.CaseTypeEntity;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

public class GetCaseTypesResponseTest {

    @Test
    public void shouldCreateGetCaseTypesResponseDTOFromCaseTypesEntity() {
        Set<CaseTypeEntity> caseTypeSet = new HashSet<>();
        caseTypeSet.add(new CaseTypeEntity(1L,"DCU Ministerial","a1","MIN","DCU", "DCU_MIN_DISPATCH", true));
        caseTypeSet.add(new CaseTypeEntity(2L,"DCU Treat Official","a2","TRO","DCU", "DCU_TRO_DISPATCH", true));
        caseTypeSet.add(new CaseTypeEntity(3L,"DCU Number 10","a3","DTEN","DCU", "DCU_DTEN_DISPATCH", true));

        GetCaseTypesResponse getCaseTypesResponse = GetCaseTypesResponse.from(caseTypeSet);

        List<CaseTypeDto> responseAsList = new ArrayList<>(Objects.requireNonNull(getCaseTypesResponse.getCaseTypes()));

        CaseTypeDto result1 = responseAsList.stream().filter(x -> "MIN".equals(x.getType())).findAny().orElse(null);
        assertThat(result1).isNotNull();
        assertThat(result1.getDisplayName()).isEqualTo("DCU Ministerial");
        CaseTypeDto result2 = responseAsList.stream().filter(x -> "TRO".equals(x.getType())).findAny().orElse(null);
        assertThat(result2).isNotNull();
        assertThat(result2.getDisplayName()).isEqualTo("DCU Treat Official");
        CaseTypeDto result3 = responseAsList.stream().filter(x -> "DTEN".equals(x.getType())).findAny().orElse(null);
        assertThat(result3).isNotNull();
        assertThat(result3.getDisplayName()).isEqualTo("DCU Number 10");
    }
}