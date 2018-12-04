package uk.gov.digital.ho.hocs.info.deadline;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.digital.ho.hocs.info.entities.Deadline;
import uk.gov.digital.ho.hocs.info.entities.HolidayDate;
import uk.gov.digital.ho.hocs.info.entities.Sla;
import uk.gov.digital.ho.hocs.info.exception.EntityNotFoundException;
import uk.gov.digital.ho.hocs.info.repositories.HolidayDateRepository;
import uk.gov.digital.ho.hocs.info.repositories.SlaRepository;
import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DeadlinesService {

    private final SlaRepository slaRepository;
    private final HolidayDateRepository holidayDateRepository;

    @Autowired
    public DeadlinesService(SlaRepository slaRepository, HolidayDateRepository holidayDateRepository) {
        this.slaRepository = slaRepository;
        this.holidayDateRepository = holidayDateRepository;
    }

    Set<Deadline> getDeadlines(String caseType, LocalDate receivedDate) {
        log.info("Requesting deadlines for caseType {} with received date of {} ", caseType, receivedDate);
        if (caseType != null && receivedDate != null) {
            Set<HolidayDate> holidays = holidayDateRepository.findAllByCaseType(caseType);
            Set<Sla> slas = slaRepository.findAllByCaseType(caseType);
            return slas.stream().map(sla -> new Deadline(receivedDate, sla, holidays)).collect(Collectors.toSet());
        } else {
            throw new EntityNotFoundException("CaseType or received date was null!");
        }
    }

    Deadline getCaseDeadlineForCaseType(String caseType, LocalDate receivedDate) {
        log.info("Requesting case deadline for caseType {} with received date of {} ", caseType,receivedDate);
        if (caseType != null && receivedDate != null) {
            Set<HolidayDate> holidays = holidayDateRepository.findAllByCaseType(caseType);
            Sla sla = slaRepository.findCaseDeadlineSlaByCaseType(caseType);
            return new Deadline(receivedDate, sla, holidays);
        } else {
            throw new EntityNotFoundException("CaseType, StageType or received date was null!");
        }
    }

    Deadline getDeadlineForStage(String stageType, LocalDate receivedDate) {
        log.info("Requesting deadline for stageType {} with received date of {} ", stageType, receivedDate);
        if (stageType != null && receivedDate != null) {
            Set<HolidayDate> holidays = holidayDateRepository.findAllByStageType(stageType);
            Sla sla = slaRepository.findAllByStageType(stageType);
            return new Deadline(receivedDate, sla, holidays);
        } else {
            throw new EntityNotFoundException("CaseType, StageType or received date was null!");
        }
    }
}