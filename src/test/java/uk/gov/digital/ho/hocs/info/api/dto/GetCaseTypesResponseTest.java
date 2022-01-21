package uk.gov.digital.ho.hocs.info.api.dto;

import org.junit.Test;
import uk.gov.digital.ho.hocs.info.domain.model.CaseType;
import uk.gov.digital.ho.hocs.info.domain.model.StageTypeEntity;

import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class GetCaseTypesResponseTest {

    @Test
    public void shouldCreateGetCaseTypesResponseDTOFromCaseTypesEntity() {
        UUID unitUUID = UUID.randomUUID();

        Set<CaseType> caseTypeSet = new HashSet<>();
        caseTypeSet.add(new CaseType(1L, UUID.randomUUID(), "DCU Ministerial","a1","MIN",unitUUID, "DCU_MIN_DISPATCH", true, true, null, new StageTypeEntity()));
        caseTypeSet.add(new CaseType(2L, UUID.randomUUID(), "DCU Treat Official","a2","TRO",unitUUID, "DCU_TRO_DISPATCH", true, true, null, new StageTypeEntity()));
        caseTypeSet.add(new CaseType(3L, UUID.randomUUID(), "DCU Number 10","a3","DTEN",unitUUID, "DCU_DTEN_DISPATCH", true, true, null, new StageTypeEntity()));

        Set<CaseTypeDto> getCaseTypesResponse = caseTypeSet.stream().map(CaseTypeDto::from).collect(Collectors.toSet());

        List<CaseTypeDto> responseAsList = new ArrayList<>(Objects.requireNonNull(getCaseTypesResponse));

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