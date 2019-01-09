package uk.gov.digital.ho.hocs.info.api.dto;

import org.junit.Test;
import uk.gov.digital.ho.hocs.info.domain.model.Deadline;
import uk.gov.digital.ho.hocs.info.domain.model.ExemptionDate;

import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

public class GetDeadlinesResponseTest {

    @Test
    public void shouldCreateGetDeadlinesResponseDTOFromCorrespondentType() {
        Set<ExemptionDate> exemptionDates = new HashSet<>();
        exemptionDates.add(new ExemptionDate(1L, LocalDate.of(2018, 1, 1)));
        Deadline deadline = new Deadline(LocalDate.of(2018, 1, 1), 5, "Draft", exemptionDates);
        Deadline deadline2 = new Deadline(LocalDate.of(2018, 2, 1), 5, "Final", exemptionDates);

        Set<Deadline> deadlinesSet = new HashSet<>();
        deadlinesSet.add(deadline);
        deadlinesSet.add(deadline2);

        GetDeadlinesResponse getDeadlinesResponse = GetDeadlinesResponse.from(deadlinesSet);

       assertThat(getDeadlinesResponse.deadlines.get("Draft")).isEqualTo(LocalDate.of(2018,01,8).toString());
       assertThat(getDeadlinesResponse.deadlines.get("Final")).isEqualTo(LocalDate.of(2018,02,8).toString());
    }
}