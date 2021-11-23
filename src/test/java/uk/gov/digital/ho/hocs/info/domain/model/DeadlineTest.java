package uk.gov.digital.ho.hocs.info.domain.model;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class DeadlineTest {

    @Test
    public void deadline_withMinusTwoSla_andCaseDeadline() {
        assertThat(Deadline.calculateDeadline(LocalDate.parse("2020-01-01"),
                LocalDate.parse("2020-01-02"), -2, Collections.emptySet()))
                .isEqualTo(LocalDate.parse("2020-01-02"));
    }

    @Test
    public void deadline_withMinusTwoSla_noCaseDeadline() {
        assertThat(Deadline.calculateDeadline(LocalDate.parse("2020-01-01"),
                null, -2, Collections.emptySet()))
                .isEqualTo(LocalDate.parse("2020-01-01"));
    }

    @Test
    public void deadline_withSlaNoExemptions_sameWeek() {
        assertThat(Deadline.calculateDeadline(LocalDate.parse("2020-01-01"), null, 2, Collections.emptySet()))
                .isEqualTo(LocalDate.parse("2020-01-03"));
    }

    @Test
    public void deadline_withSlaNoExemptions_acrossWeekend() {
        assertThat(Deadline.calculateDeadline(LocalDate.parse("2020-01-01"), null, 5, Collections.emptySet()))
                .isEqualTo(LocalDate.parse("2020-01-08"));
    }

    @Test
    public void deadline_withSlaNoExemptions_startSaturday() {
        assertThat(Deadline.calculateDeadline(LocalDate.parse("2020-01-04"), null, 3, Collections.emptySet()))
                .isEqualTo(LocalDate.parse("2020-01-09"));
    }

    @Test
    public void deadline_withSlaMondayExemption_startSaturday() {
        assertThat(Deadline.calculateDeadline(LocalDate.parse("2020-01-04"), null, 3, Set.of(new ExemptionDate(1L, LocalDate.parse("2020-01-06")))))
                .isEqualTo(LocalDate.parse("2020-01-10"));
    }

    @Test
    public void calculateRemainingWorkingDays_overWeekend() {

        LocalDate dateOfCheck = LocalDate.parse("2021-11-19"); // Fri
        LocalDate deadline = LocalDate.parse("2021-11-22"); // Mon

        assertThat(Deadline.calculateRemainingWorkingDays(dateOfCheck, deadline, Set.of())).isEqualTo(2);
    }

    @Test
    public void calculateRemainingWorkingDays_overBankHolidayWeekend() {

        LocalDate dateOfCheck = LocalDate.parse("2021-04-30"); // Fri
        LocalDate deadline = LocalDate.parse("2021-05-07"); // Mon
        LocalDate bankHolidayDay = LocalDate.parse("2021-05-03");

        assertThat(Deadline.calculateRemainingWorkingDays(dateOfCheck, deadline, Set.of(new ExemptionDate(1L, bankHolidayDay)))).isEqualTo(5);
    }

    @Test
    public void calculateRemainingWorkingDays_deadLineDayShouldReturn1() {

        LocalDate dateOfCheck = LocalDate.parse("2021-04-30");
        LocalDate deadline = LocalDate.parse("2021-04-30");
        LocalDate bankHolidayDay = LocalDate.parse("2021-05-03");

        assertThat(Deadline.calculateRemainingWorkingDays(dateOfCheck, deadline, Set.of(new ExemptionDate(1L, bankHolidayDay)))).isEqualTo(1);
    }

    @Test
    public void calculateRemainingWorkingDays_deadLinePastShouldReturn0() {

        LocalDate dateOfCheck = LocalDate.parse("2021-04-30");
        LocalDate deadline = LocalDate.parse("2021-04-29");
        LocalDate bankHolidayDay = LocalDate.parse("2021-05-03");

        assertThat(Deadline.calculateRemainingWorkingDays(dateOfCheck, deadline, Set.of(new ExemptionDate(1L, bankHolidayDay)))).isEqualTo(0);
    }
}
