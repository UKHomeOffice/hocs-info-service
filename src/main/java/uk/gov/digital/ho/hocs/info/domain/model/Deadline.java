package uk.gov.digital.ho.hocs.info.domain.model;

import lombok.Getter;
import uk.gov.digital.ho.hocs.info.utils.DateUtils;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
public class Deadline implements Serializable {

    private final LocalDate date;

    public Deadline(LocalDate receivedDate, LocalDate caseDeadline, int sla, Set<ExemptionDate> holidays) {
        this.date = calculateDeadline(receivedDate, caseDeadline, sla, holidays);
    }

    public static LocalDate calculateDeadline(LocalDate receivedDate, LocalDate caseDeadline, int sla, Set<ExemptionDate> holidays) {
        LocalDate deadline = receivedDate;
        if (sla == -2 && caseDeadline != null){
            return caseDeadline;
        }

        Set<LocalDate> holidayDates = holidays.stream().map(ExemptionDate::getDate).collect(Collectors.toSet());

        // Start deadline from the next first working day.
        while (DateUtils.isDateNonWorkingDay(deadline, holidayDates)) {
            deadline = deadline.plusDays(1);
        }

        int i = 1;
        while (i <= sla) {
            deadline = deadline.plusDays(1);
            // Only increment Mon-Fri and non-holidays
            if (!(DateUtils.isDateNonWorkingDay(deadline, holidayDates))) {
                ++i;
            }
        }
        return deadline;
    }

    public static int calculateRemainingWorkingDays(LocalDate todaysDate, LocalDate deadline, Set<ExemptionDate> holidays) {

        int daysRemaining = 0;

        Set<LocalDate> holidayDates = holidays.stream().map(ExemptionDate::getDate).collect(Collectors.toSet());

        while(todaysDate.isBefore(deadline.plusDays(1))) {
            if (!DateUtils.isDateNonWorkingDay(todaysDate, holidayDates)) {
                daysRemaining++;
            }
            todaysDate = todaysDate.plusDays(1);
        }

        return daysRemaining;
    }

}
