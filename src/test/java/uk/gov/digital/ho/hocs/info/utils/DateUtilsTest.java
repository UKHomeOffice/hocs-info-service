package uk.gov.digital.ho.hocs.info.utils;

import org.junit.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

public class DateUtilsTest {

    @Test
    public void isWeekend_null(){
        assertThat(DateUtils.isWeekend(null)).isFalse();
    }

    @Test
    public void isWeekend_monday(){
        assertThat(DateUtils.isWeekend(LocalDate.parse("2020-05-11"))).isFalse();
    }

    @Test
    public void isWeekend_tuesday(){
        assertThat(DateUtils.isWeekend(LocalDate.parse("2020-05-12"))).isFalse();
    }

    @Test
    public void isWeekend_wednesday(){
        assertThat(DateUtils.isWeekend(LocalDate.parse("2020-05-13"))).isFalse();
    }

    @Test
    public void isWeekend_thursday(){
        assertThat(DateUtils.isWeekend(LocalDate.parse("2020-05-14"))).isFalse();
    }

    @Test
    public void isWeekend_friday(){
        assertThat(DateUtils.isWeekend(LocalDate.parse("2020-05-15"))).isFalse();
    }

    @Test
    public void isWeekend_saturday(){
        assertThat(DateUtils.isWeekend(LocalDate.parse("2020-05-16"))).isTrue();
    }

    @Test
    public void isWeekend_sunday(){
        assertThat(DateUtils.isWeekend(LocalDate.parse("2020-05-17"))).isTrue();
    }
}
