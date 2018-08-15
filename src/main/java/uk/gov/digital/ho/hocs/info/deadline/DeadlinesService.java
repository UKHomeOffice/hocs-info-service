package uk.gov.digital.ho.hocs.info.deadline;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.digital.ho.hocs.info.RequestData;
import uk.gov.digital.ho.hocs.info.casetype.CaseTypeService;
import uk.gov.digital.ho.hocs.info.entities.Deadline;
import uk.gov.digital.ho.hocs.info.entities.Sla;
import uk.gov.digital.ho.hocs.info.exception.EntityNotFoundException;
import uk.gov.digital.ho.hocs.info.exception.EntityPermissionException;
import uk.gov.digital.ho.hocs.info.repositories.HolidayDateRepository;
import uk.gov.digital.ho.hocs.info.repositories.SlaRepository;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DeadlinesService {

    private final SlaRepository slaRepository;
    private final CaseTypeService caseTypeService;
    private final HolidayDateRepository holidayDateRepository;
    private final RequestData requestData;

    @Autowired
    public DeadlinesService(SlaRepository slaRepository, HolidayDateRepository holidayDateRepository, CaseTypeService caseTypeService, RequestData requestData) {
        this.slaRepository = slaRepository;
        this.holidayDateRepository = holidayDateRepository;
        this.caseTypeService = caseTypeService;
        this.requestData = requestData;
    }

    Set<Deadline> getDeadlines(String caseType, LocalDate receivedDate) throws EntityPermissionException, EntityNotFoundException {
        log.info("Requesting deadlines for caseType {} with received date of {} ", caseType, receivedDate);
        if (caseType != null && receivedDate != null) {
            if (caseTypeService.hasPermissionForCaseType(caseType)) {
                Set<LocalDate> holidays = holidayDateRepository.findAllByCaseType(caseType);
                Set<Sla> slas = slaRepository.findAllByCaseType(caseType);
                return slas.stream().map(sla -> new Deadline(receivedDate, sla, holidays)).collect(Collectors.toSet());
            } else {
                throw new EntityPermissionException("Not allowed to get deadlines caseType: %s not in Roles: %s", caseType, requestData.rolesString());
            }
        } else {
            throw new EntityNotFoundException("CaseType or received date was null!");
        }
    }
}