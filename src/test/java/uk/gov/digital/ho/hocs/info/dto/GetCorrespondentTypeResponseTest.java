package uk.gov.digital.ho.hocs.info.dto;

import org.junit.Test;
import uk.gov.digital.ho.hocs.info.entities.CaseTypeEntity;
import uk.gov.digital.ho.hocs.info.entities.CorrespondentType;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

public class GetCorrespondentTypeResponseTest {

    @Test
    public void shouldCreateGetCorrespondentTypeResponseDTOFromCorrespondentType() {
        Set<CorrespondentType> correspondentTypeSet = new HashSet<>();
        correspondentTypeSet.add(new CorrespondentType(1L,"Correspondent","CORRESPONDENT"));
        correspondentTypeSet.add(new CorrespondentType(2L,"Constituent","CONSTITUENT"));
        correspondentTypeSet.add(new CorrespondentType(3L,"Member","MEMBER"));
        GetCorrespondentTypeResponse getCorrespondentTypeResponse = GetCorrespondentTypeResponse.from(correspondentTypeSet);

        List<CorrespondentTypeDto> responseAsList = new ArrayList<>(Objects.requireNonNull(getCorrespondentTypeResponse.getCorrespondentTypes()));

        CorrespondentTypeDto result1 = responseAsList.stream().filter(x -> "CORRESPONDENT".equals(x.getType())).findAny().orElse(null);
        assertThat(result1).isNotNull();
        assertThat(result1.getDisplayName()).isEqualTo("Correspondent");
        CorrespondentTypeDto result2 = responseAsList.stream().filter(x -> "CONSTITUENT".equals(x.getType())).findAny().orElse(null);
        assertThat(result2).isNotNull();
        assertThat(result2.getDisplayName()).isEqualTo("Constituent");
        CorrespondentTypeDto result3 = responseAsList.stream().filter(x -> "MEMBER".equals(x.getType())).findAny().orElse(null);
        assertThat(result3).isNotNull();
        assertThat(result3.getDisplayName()).isEqualTo("Member");
    }
}