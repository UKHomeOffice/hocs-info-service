package uk.gov.digital.ho.hocs.info.api;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.digital.ho.hocs.info.domain.model.Deadline;
import uk.gov.digital.ho.hocs.info.domain.model.ExemptionDate;
import uk.gov.digital.ho.hocs.info.domain.model.StageTypeEntity;
import uk.gov.digital.ho.hocs.info.domain.repository.HolidayDateRepository;
import uk.gov.digital.ho.hocs.info.domain.repository.StageTypeRepository;

import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DeadlinesServiceTest {

    @Mock
    private HolidayDateRepository holidayDateRepository;

    @Mock
    private StageTypeRepository stageTypeRepository;

    private StageTypeService service;

    @Before
    public void setup()
    {
        service = new StageTypeService(stageTypeRepository, holidayDateRepository);
    }


    @Test
    public void shouldCalculateDeadlinesWhenThreeDaySlaNotSpanningOverWeekend()  {

        when(holidayDateRepository.findAllByStageType(any())).thenReturn(getHolidays());
        when(stageTypeRepository.findByType("final")).thenReturn(get3DaySla());

        Deadline deadline = service.getDeadlineForStageType("final", LocalDate.of(2018, 1, 2));

        verify(holidayDateRepository, times(1)).findAllByStageType(any());

        assertThat(deadline.getDate()).isEqualTo(LocalDate.of(2018, 1, 5));
        assertThat(deadline.getType()).isEqualTo("final");
    }

    @Test
    public void shouldCalculateDeadlinesWhenThreeDaySlaSpanningOverTwoWeekendDays()  {
        when(holidayDateRepository.findAllByStageType(any())).thenReturn(getHolidays());
        when(stageTypeRepository.findByType("final")).thenReturn(get3DaySla());

        Deadline deadline = service.getDeadlineForStageType("final", LocalDate.of(2018, 1, 5));

        verify(holidayDateRepository, times(1)).findAllByStageType(any());

        assertThat(deadline.getDate()).isEqualTo(LocalDate.of(2018, 1, 10));
        assertThat(deadline.getType()).isEqualTo("final");
    }

    @Test
    public void shouldCalculateDeadlinesWhenThreeDaySlaSpanningOverTwoWeekendAndOneHolidayMonday()  {

        when(holidayDateRepository.findAllByStageType(any())).thenReturn(getHolidays());
        when(stageTypeRepository.findByType("final")).thenReturn(get3DaySla());

        Deadline deadline = service.getDeadlineForStageType("final", LocalDate.of(2018, 1, 12));

        verify(holidayDateRepository, times(1)).findAllByStageType(any());

        assertThat(deadline.getDate()).isEqualTo(LocalDate.of(2018, 1, 18));
        assertThat(deadline.getType()).isEqualTo("final");
    }

    @Test
    public void shouldCalculateDeadlinesWhenTenDaySlaSpanningOverSixWeekendDaysAndTwoHolidayDays()  {
        when(holidayDateRepository.findAllByStageType(any())).thenReturn(getHolidays());
        when(stageTypeRepository.findByType("final")).thenReturn(get10DaySla());

        Deadline deadline = service.getDeadlineForStageType("final", LocalDate.of(2018, 12, 13));

        verify(holidayDateRepository, times(1)).findAllByStageType(any());

        assertThat(deadline.getDate()).isEqualTo(LocalDate.of(2018, 12, 31));
        assertThat(deadline.getType()).isEqualTo("final");
    }

    @Test
    public void shouldCalculateDeadlinesWhenTenDaySlaSpanningOverSixWeekendDaysAndThreeHolidayDaysOverEndOfYear()  {

        when(holidayDateRepository.findAllByStageType(any())).thenReturn(getHolidays());
        when(stageTypeRepository.findByType("final")).thenReturn(get10DaySla());

        Deadline deadline = service.getDeadlineForStageType("final", LocalDate.of(2018, 12, 20));

        verify(holidayDateRepository, times(1)).findAllByStageType(any());

        assertThat(deadline.getDate()).isEqualTo(LocalDate.of(2019, 1, 8));
        assertThat(deadline.getType()).isEqualTo("final");
    }


    @Test
    public void shouldCalculateDeadlinesWhenTenDaySlaSpanningOver29February2020LeapYear()  {

        when(holidayDateRepository.findAllByStageType(any())).thenReturn(getHolidays());
        when(stageTypeRepository.findByType("final")).thenReturn(get10DaySla());

        Deadline deadline = service.getDeadlineForStageType("final", LocalDate.of(2020, 2, 20));

        verify(holidayDateRepository, times(1)).findAllByStageType(any());

        assertThat(deadline.getDate()).isEqualTo(LocalDate.of(2020, 3, 5));
        assertThat(deadline.getType()).isEqualTo("final");
}

    private static Set<ExemptionDate> getHolidays() {
        Set<ExemptionDate> holidays = new HashSet<>();

        holidays.add(new ExemptionDate(1L,LocalDate.of(2018, 1, 15)));
        holidays.add(new ExemptionDate(2L,LocalDate.of(2018, 12, 25)));
        holidays.add(new ExemptionDate(3L,LocalDate.of(2018, 12, 26)));
        holidays.add(new ExemptionDate(4L,LocalDate.of(2019, 1, 1)));
        return holidays;
    }

    private static StageTypeEntity get3DaySla() {
        return new StageTypeEntity(1L, UUID.randomUUID(), "stage name", "111","STAGE_TYPE", UUID.randomUUID(),3,true, null);
    }

    private static StageTypeEntity get10DaySla() {
        return new StageTypeEntity(1L, UUID.randomUUID(), "stage name", "111","STAGE_TYPE", UUID.randomUUID(),10,true, null);

    }
}