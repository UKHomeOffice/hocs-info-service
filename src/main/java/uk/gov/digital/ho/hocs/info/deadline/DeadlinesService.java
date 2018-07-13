package uk.gov.digital.ho.hocs.info.deadline;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.digital.ho.hocs.info.repositories.HolidayRepository;
import uk.gov.digital.ho.hocs.info.repositories.SlaRepository;
import uk.gov.digital.ho.hocs.info.entities.Holiday;
import uk.gov.digital.ho.hocs.info.entities.Sla;
import uk.gov.digital.ho.hocs.info.dto.Deadline;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DeadlinesService {

    private final SlaRepository slaRepository;
    private final HolidayRepository holidayRepository;

    @Autowired
    public DeadlinesService(SlaRepository slaRepository, HolidayRepository holidayRepository) {
        this.slaRepository = slaRepository;
        this.holidayRepository = holidayRepository;
    }

    public Set<Deadline> getDeadlines(String caseTypeDisplayName, LocalDate receivedDate) {

        List<Holiday> holidays = holidayRepository.findAllByCaseType(caseTypeDisplayName);

        List<Sla> slas = slaRepository.findSLACaseType(caseTypeDisplayName);

        return calculateDeadline(receivedDate, slas, holidays);

    }

    private Set<Deadline> calculateDeadline(LocalDate receivedDate, List<Sla> slas, List<Holiday> holidays) {
        Set<Deadline> deadlines = new HashSet<>();
        for (Sla sla : slas) {
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
            deadlines.add(new Deadline(sla.getType(),deadlineDate));
        }
        return deadlines;
    }

}