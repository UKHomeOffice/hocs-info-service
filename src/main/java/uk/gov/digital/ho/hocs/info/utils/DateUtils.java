package uk.gov.digital.ho.hocs.info.utils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Set;

public final class DateUtils {

    private static boolean isWeekend(LocalDate date) {
        return date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY;
    }

    public static boolean isDateNonWorkingDay(LocalDate date, Set<LocalDate> holidayDates) {
        if (date == null || holidayDates == null) {
            return false;
        }

        return (DateUtils.isWeekend(date) || holidayDates.contains(date));
    }
}
