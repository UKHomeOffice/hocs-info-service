package uk.gov.digital.ho.hocs.info.api.dto;

import org.junit.Test;
import uk.gov.digital.ho.hocs.info.domain.model.Deadline;
import uk.gov.digital.ho.hocs.info.domain.model.HolidayDate;
import uk.gov.digital.ho.hocs.info.domain.model.Sla;

import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

public class GetDeadlinesResponseTest {

    @Test
    public void shouldCreateGetDeadlinesResponseDTOFromCorrespondentType() {
        Sla sla = new Sla("Draft", 5, "MIN");
        Sla sla2 = new Sla("Final", 5, "MIN");
        Set<HolidayDate> holidayDates = new HashSet<>();
        holidayDates.add(new HolidayDate(1L, LocalDate.of(2018, 1, 1)));
        Deadline deadline = new Deadline(LocalDate.of(2018, 1, 1), sla, holidayDates);
        Deadline deadline2 = new Deadline(LocalDate.of(2018, 2, 1), sla2, holidayDates);

        Set<Deadline> deadlinesSet = new HashSet<>();
        deadlinesSet.add(deadline);
        deadlinesSet.add(deadline2);

        GetDeadlinesResponse getDeadlinesResponse = GetDeadlinesResponse.from(deadlinesSet);

       assertThat(getDeadlinesResponse.deadlines.get("Draft")).isEqualTo(LocalDate.of(2018,01,8).toString());
       assertThat(getDeadlinesResponse.deadlines.get("Final")).isEqualTo(LocalDate.of(2018,02,8).toString());
    }
}