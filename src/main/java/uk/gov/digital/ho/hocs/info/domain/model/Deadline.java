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
    private final String type;

    public Deadline(LocalDate receivedDate, int sla, String type, Set<ExemptionDate> holidays) {
        this.date = calculateDeadline(receivedDate, sla, holidays);
        this.type = type;
    }

    public static LocalDate calculateDeadline(LocalDate receivedDate, int sla, Set<ExemptionDate> holidays) {
        LocalDate deadline = receivedDate;
        Set<LocalDate> holidayDates = holidays.stream().map(ExemptionDate::getDate).collect(Collectors.toSet());
        int i = 0;
        while (i < sla) {
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
