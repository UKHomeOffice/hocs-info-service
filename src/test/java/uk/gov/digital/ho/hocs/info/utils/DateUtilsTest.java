package uk.gov.digital.ho.hocs.info.utils;

import org.junit.Test;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class DateUtilsTest {

    private final Set<LocalDate> nonWorkingDateFriday = Set.of(LocalDate.parse("2020-01-01"));
    private final Set<LocalDate> nonWorkingDateOther = Set.of(LocalDate.parse("2020-01-02"));

    @Test
    public void isDateNonWorkingDay_dateNull(){
        assertThat(DateUtils.isDateNonWorkingDay(null, Collections.emptySet())).isFalse();
    }

    @Test
    public void isDateNonWorkingDay_exemptionsNull(){
        assertThat(DateUtils.isDateNonWorkingDay(LocalDate.parse("2020-01-01"), null)).isFalse();
    }

    @Test
    public void isDateNonWorkingDay_mondayDate_emptyExemption(){
        assertThat(DateUtils.isDateNonWorkingDay(LocalDate.parse("2020-05-11"), Collections.emptySet())).isFalse();
    }

    @Test
    public void isDateNonWorkingDay_tuesdayDate_emptyExemption(){
        assertThat(DateUtils.isDateNonWorkingDay(LocalDate.parse("2020-05-12"), Collections.emptySet())).isFalse();
    }

    @Test
    public void isDateNonWorkingDay_wednesdayDate_emptyExemption(){
        assertThat(DateUtils.isDateNonWorkingDay(LocalDate.parse("2020-05-13"), Collections.emptySet())).isFalse();
    }

    @Test
    public void isDateNonWorkingDay_thursdayDate_emptyExemption(){
        assertThat(DateUtils.isDateNonWorkingDay(LocalDate.parse("2020-05-14"), Collections.emptySet())).isFalse();
    }

    @Test
    public void isDateNonWorkingDay_fridayDate_emptyExemption(){
        assertThat(DateUtils.isDateNonWorkingDay(LocalDate.parse("2020-05-15"), Collections.emptySet())).isFalse();
    }

    @Test
    public void isDateNonWorkingDay_saturdayDate_emptyExemption(){
        assertThat(DateUtils.isDateNonWorkingDay(LocalDate.parse("2020-05-16"), Collections.emptySet())).isTrue();
    }

    @Test
    public void isDateNonWorkingDay_sundayDate_emptyExemption(){
        assertThat(DateUtils.isDateNonWorkingDay(LocalDate.parse("2020-05-17"), Collections.emptySet())).isTrue();
    }

    @Test
    public void isDateNonWorkingDay_fridayDate_exempt(){
        assertThat(DateUtils.isDateNonWorkingDay(LocalDate.parse("2020-01-01"), nonWorkingDateFriday)).isTrue();
    }

    @Test
    public void isDateNonWorkingDay_fridayDate_exemptOther(){
        assertThat(DateUtils.isDateNonWorkingDay(LocalDate.parse("2020-01-01"), nonWorkingDateOther)).isFalse();
    }
}
