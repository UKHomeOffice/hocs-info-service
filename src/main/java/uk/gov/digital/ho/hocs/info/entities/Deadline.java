package uk.gov.digital.ho.hocs.info.entities;

import lombok.Getter;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
public class Deadline implements Serializable {

    private String type;

    private LocalDate date;

    public Deadline(LocalDate receivedDate, Sla sla, Set<HolidayDate> holidays) {
        type = sla.getStageType();
        date = calculateDeadline(receivedDate, sla.getValue(), holidays);
    }

    private static LocalDate calculateDeadline(LocalDate receivedDate, int sla, Set<HolidayDate> holidays) {
        LocalDate deadline = receivedDate;
        Set<LocalDate> holidayDates = holidays.stream().map(HolidayDate::getDate).collect(Collectors.toSet());
        int i = 0;
        while (i < sla) {
            deadline = deadline.plusDays(1);
            if (!(isWeekend(deadline) || holidayDates.contains(deadline))) {
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
