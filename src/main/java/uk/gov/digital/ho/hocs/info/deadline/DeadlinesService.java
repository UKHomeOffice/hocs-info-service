package uk.gov.digital.ho.hocs.info.deadline;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.digital.ho.hocs.info.dto.DeadlineDto;
import uk.gov.digital.ho.hocs.info.entities.Holiday;
import uk.gov.digital.ho.hocs.info.entities.Sla;
import uk.gov.digital.ho.hocs.info.exception.EntityNotFoundException;
import uk.gov.digital.ho.hocs.info.repositories.HolidayRepository;
import uk.gov.digital.ho.hocs.info.repositories.SlaRepository;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static uk.gov.digital.ho.hocs.info.HocsInfoServiceApplication.isNullOrEmpty;

@Service
public class DeadlinesService {

    private final SlaRepository slaRepository;
    private final HolidayRepository holidayRepository;

    @Autowired
    public DeadlinesService(SlaRepository slaRepository, HolidayRepository holidayRepository) {
        this.slaRepository = slaRepository;
        this.holidayRepository = holidayRepository;
    }

    public Set<DeadlineDto> getDeadlines(String type, LocalDate receivedDate) {
        if (!isNullOrEmpty(type) && !isNullOrEmpty(receivedDate)) {
            List<Holiday> holidays = holidayRepository.findAllByCaseType(type);

            List<Sla> slas = slaRepository.findSLACaseType(type);

            if (holidays != null && slas != null) {
                return calculateDeadline(receivedDate, slas, holidays);
            } else {
                throw new EntityNotFoundException("Holidays or SLAs not Found!");
            }
        } else {
            throw new EntityNotFoundException("Type or received date was null!");
        }
    }

    private Set<DeadlineDto> calculateDeadline(LocalDate receivedDate, List<Sla> slas, List<Holiday> holidays) {
        Set<DeadlineDto> deadlineDtos = new HashSet<>();
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
            deadlineDtos.add(new DeadlineDto(sla.getType(), deadlineDate));
        }
        return deadlineDtos;
    }

}