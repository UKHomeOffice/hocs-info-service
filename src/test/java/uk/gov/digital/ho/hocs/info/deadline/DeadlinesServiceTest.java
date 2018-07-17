package uk.gov.digital.ho.hocs.info.deadline;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.digital.ho.hocs.info.dto.DeadlineDto;
import uk.gov.digital.ho.hocs.info.entities.Holiday;
import uk.gov.digital.ho.hocs.info.entities.Sla;
import uk.gov.digital.ho.hocs.info.repositories.HolidayRepository;
import uk.gov.digital.ho.hocs.info.repositories.SlaRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DeadlinesServiceTest {

    @Mock
    private HolidayRepository holidayRepository;

    @Mock
    private SlaRepository slaRepository;

    private DeadlinesService deadlinesService;

    private static final String CASE_TYPE_TYPE = "MIN";

    @Before
    public void setUp() {
        this.deadlinesService = new DeadlinesService(slaRepository, holidayRepository);
    }

    @Test
    public void shouldCalculateDeadlinesWhenThreeDaySlaNotSpanningOverWeekend() {

        when(holidayRepository.findAllByCaseType(any())).thenReturn(getHolidays());
        when(slaRepository.findSLACaseType(any())).thenReturn(get3DaySla());

        Set<DeadlineDto> deadlineDtos =  deadlinesService.getDeadlines(CASE_TYPE_TYPE, LocalDate.of(2018, 01, 02));

        List<DeadlineDto> deadlinesAsList = new ArrayList<>(deadlineDtos);

        verify(holidayRepository, times(1)).findAllByCaseType(any());
        verify(slaRepository, times(1)).findSLACaseType(any());

        assertThat(deadlinesAsList.get(0).getDate()).isEqualTo(LocalDate.of(2018, 01, 05));
        assertThat(deadlinesAsList.get(0).getType()).isEqualTo("dispatch");
    }
    @Test
    public void shouldCalculateDeadlinesWhenThreeDaySlaSpanningOverTwoWeekendDays() {

        when(holidayRepository.findAllByCaseType(any())).thenReturn(getHolidays());
        when(slaRepository.findSLACaseType(any())).thenReturn(get3DaySla());

        Set<DeadlineDto> deadlineDtos =  deadlinesService.getDeadlines(CASE_TYPE_TYPE, LocalDate.of(2018, 01, 05));

        List<DeadlineDto> deadlinesAsList = new ArrayList<>(deadlineDtos);

        verify(holidayRepository, times(1)).findAllByCaseType(any());
        verify(slaRepository, times(1)).findSLACaseType(any());
        assertThat(deadlinesAsList.get(0).getDate()).isEqualTo(LocalDate.of(2018, 01, 10));
        assertThat(deadlinesAsList.get(0).getType()).isEqualTo("dispatch");

    }

    @Test
    public void shouldCalculateDeadlinesWhenThreeDaySlaSpanningOverTwoWeekendAndOneHolidayMonday() {

        when(holidayRepository.findAllByCaseType(any())).thenReturn(getHolidays());
        when(slaRepository.findSLACaseType(any())).thenReturn(get3DaySla());

        Set<DeadlineDto> deadlineDtos =  deadlinesService.getDeadlines(CASE_TYPE_TYPE, LocalDate.of(2018, 01, 12));

        List<DeadlineDto> deadlinesAsList = new ArrayList<>(deadlineDtos);

        verify(holidayRepository, times(1)).findAllByCaseType(any());
        verify(slaRepository, times(1)).findSLACaseType(any());
        assertThat(deadlinesAsList.get(0).getDate()).isEqualTo(LocalDate.of(2018, 01, 18));
        assertThat(deadlinesAsList.get(0).getType()).isEqualTo("dispatch");
    }

    @Test
    public void shouldCalculateDeadlinesWhenTenDaySlaSpanningOverSixWeekendDaysAndTwoHolidayDays() {

        when(holidayRepository.findAllByCaseType(any())).thenReturn(getHolidays());
        when(slaRepository.findSLACaseType(any())).thenReturn(get10DaySla());

        Set<DeadlineDto> deadlineDtos =  deadlinesService.getDeadlines(CASE_TYPE_TYPE, LocalDate.of(2018, 12, 13));

        List<DeadlineDto> deadlinesAsList = new ArrayList<>(deadlineDtos);

        verify(holidayRepository, times(1)).findAllByCaseType(any());
        verify(slaRepository, times(1)).findSLACaseType(any());
        assertThat(deadlinesAsList.get(0).getDate()).isEqualTo(LocalDate.of(2018, 12, 31));
        assertThat(deadlinesAsList.get(0).getType()).isEqualTo("dispatch");
    }

    @Test
    public void shouldCalculateDeadlinesWhenTenDaySlaSpanningOverSixWeekendDaysAndThreeHolidayDaysOverEndOfYear() {

        when(holidayRepository.findAllByCaseType(any())).thenReturn(getHolidays());
        when(slaRepository.findSLACaseType(any())).thenReturn(get10DaySla());

        Set<DeadlineDto> deadlineDtos =  deadlinesService.getDeadlines(CASE_TYPE_TYPE, LocalDate.of(2018, 12, 20));

        List<DeadlineDto> deadlinesAsList = new ArrayList<>(deadlineDtos);

        verify(holidayRepository, times(1)).findAllByCaseType(any());
        verify(slaRepository, times(1)).findSLACaseType(any());
        assertThat(deadlinesAsList.get(0).getDate()).isEqualTo(LocalDate.of(2019, 01, 8));
        assertThat(deadlinesAsList.get(0).getType()).isEqualTo("dispatch");
    }

    @Test
    public void shouldCalculateDeadlinesWhenTenDaySlaSpanningOver29February2020LeapYear() {

        when(holidayRepository.findAllByCaseType(any())).thenReturn(getHolidays());
        when(slaRepository.findSLACaseType(any())).thenReturn(get10DaySla());

        Set<DeadlineDto> deadlineDtos =  deadlinesService.getDeadlines(CASE_TYPE_TYPE, LocalDate.of(2020, 02, 20));

        List<DeadlineDto> deadlinesAsList = new ArrayList<>(deadlineDtos);

        verify(holidayRepository, times(1)).findAllByCaseType(any());
        verify(slaRepository, times(1)).findSLACaseType(any());
        assertThat(deadlinesAsList.get(0).getDate()).isEqualTo(LocalDate.of(2020, 03, 5));
        assertThat(deadlinesAsList.get(0).getType()).isEqualTo("dispatch");
    }

    private List<Holiday> getHolidays() {
        List<Holiday> holidays = new ArrayList<>();
        Holiday holiday1 = new Holiday();
        holiday1.setId(1);
        holiday1.setDate(LocalDate.of(2018, 01, 15));
        holidays.add(holiday1);
        Holiday holiday2 = new Holiday();
        holiday2.setId(2);
        holidays.add(holiday2);
        holiday2.setDate(LocalDate.of(2018, 12, 25));
        Holiday holiday3 = new Holiday();
        holiday3.setId(3);
        holidays.add(holiday3);
        holiday3.setDate(LocalDate.of(2018, 12, 26));
        Holiday holiday4 = new Holiday();
        holiday4.setId(4);
        holiday4.setDate(LocalDate.of(2019, 01, 01));
        holidays.add(holiday4);
        return holidays;
    }
    private List<Sla> get3DaySla() {
        List<Sla> slas = new ArrayList<>();
        Sla sla1 = new Sla();
        sla1.setId(1);
        sla1.setType("dispatch");
        sla1.setValue(3l);
        slas.add(sla1);
        return slas;
    }

    private List<Sla> get10DaySla() {
        List<Sla> slas = new ArrayList<>();
        Sla sla1 = new Sla();
        sla1.setId(1);
        sla1.setType("dispatch");
        sla1.setValue(10l);
        slas.add(sla1);
        return slas;
    }
}