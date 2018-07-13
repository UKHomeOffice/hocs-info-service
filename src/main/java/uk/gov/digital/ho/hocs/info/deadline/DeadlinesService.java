package uk.gov.digital.ho.hocs.info.deadline;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.digital.ho.hocs.info.Repositories.HolidayRepository;
import uk.gov.digital.ho.hocs.info.Repositories.SLARepository;
import uk.gov.digital.ho.hocs.info.entities.Holiday;
import uk.gov.digital.ho.hocs.info.entities.SLA;
import uk.gov.digital.ho.hocs.info.model.Deadline;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DeadlinesService {

    private final SLARepository slaRepository;
    private final HolidayRepository holidayRepository;

    @Autowired
    public DeadlinesService(SLARepository slaRepository, HolidayRepository holidayRepository) {
        this.slaRepository = slaRepository;
        this.holidayRepository = holidayRepository;
    }

    public Set<Deadline> getDeadlines(String caseTypeDisplayName, LocalDate receivedDate) {

        List<Holiday> holidays = holidayRepository.findAllByCaseType(caseTypeDisplayName);

        List<SLA> slas = slaRepository.findSLACaseType(caseTypeDisplayName);

        return calculateDeadline(receivedDate, slas, holidays);

    }

    private Set<Deadline> calculateDeadline(LocalDate receivedDate, List<SLA> slas, List<Holiday> holidays) {
        Set<Deadline> deadlines = new HashSet<>();
        for (SLA sla : slas) {
            Deadline deadline = new Deadline();
            LocalDate deadlineDate = receivedDate;
            int addedDays = 0;
            while (addedDays < sla.getValue()) {
                deadlineDate = deadlineDate.plusDays(1);
                if (!(deadlineDate.getDayOfWeek() == DayOfWeek.SATURDAY ||
                        deadlineDate.getDayOfWeek() == DayOfWeek.SUNDAY ||
                        (holidays.stream().map(Holiday::getDate).collect(Collectors.toList()).contains(deadlineDate)))) {
                    ++addedDays;
                }
            }
            deadline.setType(sla.getType());
            deadline.setDate(deadlineDate);
            deadlines.add(deadline);
        }
        return deadlines;
    }

}