package uk.gov.digital.ho.hocs.info.entities;

import lombok.Getter;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Set;

@Getter
public class Deadline {

    private String type;

    private LocalDate date;

    public Deadline(LocalDate receivedDate, Sla sla, Set<LocalDate> holidays) {
        type = sla.getStageType();
        date = calculateDeadline(receivedDate, sla.getValue(), holidays);
    }

    private static LocalDate calculateDeadline(LocalDate receivedDate, int sla, Set<LocalDate> holidays) {
        LocalDate deadline = receivedDate;

        int i = 0;
        while (i < sla) {
            deadline = deadline.plusDays(1);
            if (!(isWeekend(deadline) || holidays.contains(deadline))) {
                // Only increment Mon-Fri and non-holidays
                ++i;
            }
        }
        return deadline;
    }

    private static boolean isWeekend(LocalDate date) {
        return date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY;
    }
}
