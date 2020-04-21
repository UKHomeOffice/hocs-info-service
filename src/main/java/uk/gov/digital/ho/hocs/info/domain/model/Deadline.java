package uk.gov.digital.ho.hocs.info.domain.model;

import lombok.Getter;

import java.io.Serializable;
import java.time.DayOfWeek;
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
        int i = 1;
        while (i <= sla) {
            deadline = deadline.plusDays(1);
            // Only increment Mon-Fri and non-holidays
            if (!(isWeekend(deadline) || holidayDates.contains(deadline))) {
                ++i;
            }
        }
        return deadline;
    }

    private static boolean isWeekend(LocalDate date) {
        return date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY;
    }
}
