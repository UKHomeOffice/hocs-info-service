package uk.gov.digital.ho.hocs.info.deadline;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.digital.ho.hocs.info.RequestData;
import uk.gov.digital.ho.hocs.info.casetype.CaseTypeService;
import uk.gov.digital.ho.hocs.info.entities.Deadline;
import uk.gov.digital.ho.hocs.info.entities.Sla;
import uk.gov.digital.ho.hocs.info.exception.EntityNotFoundException;
import uk.gov.digital.ho.hocs.info.exception.EntityPermissionException;
import uk.gov.digital.ho.hocs.info.repositories.HolidayDateRepository;
import uk.gov.digital.ho.hocs.info.repositories.SlaRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Ignore
@RunWith(MockitoJUnitRunner.class)
public class DeadlinesServiceTest {

    @Mock
    private HolidayDateRepository holidayDateRepository;

    @Mock
    private SlaRepository slaRepository;

    @Mock
    private CaseTypeService caseTypeService;

    @Mock
    private RequestData requestData;

    private DeadlinesService deadlinesService;

    private static final String CASE_TYPE_TYPE = "MIN";

    @Before
    public void setUp() {
        this.deadlinesService = new DeadlinesService(slaRepository, holidayDateRepository, caseTypeService, requestData);
    }

    @Test
    public void shouldCalculateDeadlinesWhenThreeDaySlaNotSpanningOverWeekend() throws EntityPermissionException, EntityNotFoundException {

        when(holidayDateRepository.findAllByCaseType(any())).thenReturn(getHolidays());
        when(slaRepository.findAllByCaseType(any())).thenReturn(get3DaySla());
        when(caseTypeService.hasPermissionForCaseType(any())).thenReturn(true);

        Set<Deadline> deadlineDtos = deadlinesService.getDeadlines(CASE_TYPE_TYPE, LocalDate.of(2018, 01, 02));

        List<Deadline> deadlinesAsList = new ArrayList<>(deadlineDtos);

        verify(holidayDateRepository, times(1)).findAllByCaseType(any());
        verify(slaRepository, times(1)).findAllByCaseType(any());

        assertThat(deadlinesAsList.get(0).getDate()).isEqualTo(LocalDate.of(2018, 01, 05));
        assertThat(deadlinesAsList.get(0).getType()).isEqualTo("final");
    }

    @Test
    public void shouldCalculateDeadlinesWhenThreeDaySlaSpanningOverTwoWeekendDays() throws EntityPermissionException, EntityNotFoundException {

        when(holidayDateRepository.findAllByCaseType(any())).thenReturn(getHolidays());
        when(slaRepository.findAllByCaseType(any())).thenReturn(get3DaySla());
        when(caseTypeService.hasPermissionForCaseType(any())).thenReturn(true);

        Set<Deadline> deadlineDtos = deadlinesService.getDeadlines(CASE_TYPE_TYPE, LocalDate.of(2018, 01, 05));

        List<Deadline> deadlinesAsList = new ArrayList<>(deadlineDtos);

        verify(holidayDateRepository, times(1)).findAllByCaseType(any());
        verify(slaRepository, times(1)).findAllByCaseType(any());
        assertThat(deadlinesAsList.get(0).getDate()).isEqualTo(LocalDate.of(2018, 01, 10));
        assertThat(deadlinesAsList.get(0).getType()).isEqualTo("final");

    }

    @Test
    public void shouldCalculateDeadlinesWhenThreeDaySlaSpanningOverTwoWeekendAndOneHolidayMonday() throws EntityPermissionException, EntityNotFoundException {

        when(holidayDateRepository.findAllByCaseType(any())).thenReturn(getHolidays());
        when(slaRepository.findAllByCaseType(any())).thenReturn(get3DaySla());
        when(caseTypeService.hasPermissionForCaseType(any())).thenReturn(true);

        Set<Deadline> deadlineDtos = deadlinesService.getDeadlines(CASE_TYPE_TYPE, LocalDate.of(2018, 01, 12));

        List<Deadline> deadlinesAsList = new ArrayList<>(deadlineDtos);

        verify(holidayDateRepository, times(1)).findAllByCaseType(any());
        verify(slaRepository, times(1)).findAllByCaseType(any());
        assertThat(deadlinesAsList.get(0).getDate()).isEqualTo(LocalDate.of(2018, 01, 18));
        assertThat(deadlinesAsList.get(0).getType()).isEqualTo("final");
    }

    @Test
    public void shouldCalculateDeadlinesWhenTenDaySlaSpanningOverSixWeekendDaysAndTwoHolidayDays() throws EntityPermissionException, EntityNotFoundException {

        when(holidayDateRepository.findAllByCaseType(any())).thenReturn(getHolidays());
        when(slaRepository.findAllByCaseType(any())).thenReturn(get10DaySla());
        when(caseTypeService.hasPermissionForCaseType(any())).thenReturn(true);

        Set<Deadline> deadlineDtos = deadlinesService.getDeadlines(CASE_TYPE_TYPE, LocalDate.of(2018, 12, 13));

        List<Deadline> deadlinesAsList = new ArrayList<>(deadlineDtos);

        verify(holidayDateRepository, times(1)).findAllByCaseType(any());
        verify(slaRepository, times(1)).findAllByCaseType(any());
        assertThat(deadlinesAsList.get(0).getDate()).isEqualTo(LocalDate.of(2018, 12, 31));
        assertThat(deadlinesAsList.get(0).getType()).isEqualTo("final");
    }

    @Test
    public void shouldCalculateDeadlinesWhenTenDaySlaSpanningOverSixWeekendDaysAndThreeHolidayDaysOverEndOfYear() throws EntityPermissionException, EntityNotFoundException {

        when(holidayDateRepository.findAllByCaseType(any())).thenReturn(getHolidays());
        when(slaRepository.findAllByCaseType(any())).thenReturn(get10DaySla());
        when(caseTypeService.hasPermissionForCaseType(any())).thenReturn(true);

        Set<Deadline> deadlineDtos = deadlinesService.getDeadlines(CASE_TYPE_TYPE, LocalDate.of(2018, 12, 20));

        List<Deadline> deadlinesAsList = new ArrayList<>(deadlineDtos);

        verify(holidayDateRepository, times(1)).findAllByCaseType(any());
        verify(slaRepository, times(1)).findAllByCaseType(any());
        assertThat(deadlinesAsList.get(0).getDate()).isEqualTo(LocalDate.of(2019, 01, 8));
        assertThat(deadlinesAsList.get(0).getType()).isEqualTo("final");
    }


    @Test
    public void shouldCalculateDeadlinesWhenTenDaySlaSpanningOver29February2020LeapYear() throws EntityPermissionException, EntityNotFoundException {

        when(holidayDateRepository.findAllByCaseType(any())).thenReturn(getHolidays());
        when(slaRepository.findAllByCaseType(any())).thenReturn(get10DaySla());
        when(caseTypeService.hasPermissionForCaseType(any())).thenReturn(true);

        Set<Deadline> deadlineDtos = deadlinesService.getDeadlines(CASE_TYPE_TYPE, LocalDate.of(2020, 02, 20));

        List<Deadline> deadlinesAsList = new ArrayList<>(deadlineDtos);

        verify(holidayDateRepository, times(1)).findAllByCaseType(any());
        verify(slaRepository, times(1)).findAllByCaseType(any());
        assertThat(deadlinesAsList.get(0).getDate()).isEqualTo(LocalDate.of(2020, 03, 5));
        assertThat(deadlinesAsList.get(0).getType()).isEqualTo("final");
    }

    private static Set<LocalDate> getHolidays() {
        Set<LocalDate> holidays = new HashSet<>();

        holidays.add(LocalDate.of(2018, 01, 15));
        holidays.add(LocalDate.of(2018, 12, 25));
        holidays.add(LocalDate.of(2018, 12, 26));
        holidays.add(LocalDate.of(2019, 01, 01));
        return holidays;
    }

    private static Set<Sla> get3DaySla() {
        Set<Sla> slas = new HashSet<>();
        Sla sla1 = new Sla("final", 3, "MIN");
        slas.add(sla1);
        return slas;
    }

    private static Set<Sla> get10DaySla() {
        Set<Sla> slas = new HashSet<>();
        Sla sla1 = new Sla("final", 10, "MIN");
        slas.add(sla1);
        return slas;
    }
}