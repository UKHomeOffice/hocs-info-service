package uk.gov.digital.ho.hocs.info.utils;

import java.time.DayOfWeek;
import java.time.LocalDate;

public final class DateUtils {

    public static boolean isWeekend(LocalDate date) {
        if (date == null) {
            return false;
        }
        return date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY;
    }
}
