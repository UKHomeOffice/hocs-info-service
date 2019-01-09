package uk.gov.digital.ho.hocs.info.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import uk.gov.digital.ho.hocs.info.domain.exception.ApplicationExceptions;
import uk.gov.digital.ho.hocs.info.domain.model.*;
import uk.gov.digital.ho.hocs.info.domain.repository.CaseTypeRepository;
import uk.gov.digital.ho.hocs.info.domain.repository.HolidayDateRepository;
import uk.gov.digital.ho.hocs.info.security.UserPermissionsService;

@Service
@Slf4j
public class CaseTypeService {

    private final CaseTypeRepository caseTypeRepository;
    private final HolidayDateRepository holidayDateRepository;
    private final StageTypeService stageTypeService;
    private final UserPermissionsService userPermissionsService;

    @Autowired
    public CaseTypeService(CaseTypeRepository caseTypeRepository, HolidayDateRepository holidayDateRepository, StageTypeService stageTypeService, UserPermissionsService userPermissionsService) {
        this.caseTypeRepository = caseTypeRepository;
        this.holidayDateRepository = holidayDateRepository;
        this.stageTypeService = stageTypeService;
        this.userPermissionsService = userPermissionsService;
    }

    Set<CaseType> getAllCaseTypes() {
        log.debug("Getting all CaseTypes");
        Set<CaseType> caseTypes = caseTypeRepository.findAll();
        log.info("Got {} CaseTypes", caseTypes.size());
        return caseTypes;
    }

    Set<CaseType> getAllCaseTypesForUser(boolean bulkOnly) {
        log.debug("Getting case types by User, bulkOnly = {}", bulkOnly);
        Set<String> userTeams = userPermissionsService.getUserTeams();
        log.debug("Found {} teams", userTeams.size());
        if(userTeams.isEmpty()) {
            log.warn("No Teams - Returning 0 CaseTypes");
            return new HashSet<>(0);
        } else {
            Set<CaseType> caseTypes;
            if (bulkOnly) {
                caseTypes = caseTypeRepository.findAllBulkCaseTypesByTeam(userTeams);
            } else {
                caseTypes = caseTypeRepository.findAllCaseTypesByTeam(userTeams);
            }
            log.info("Got {} CaseTypes (bulkOnly = {}", caseTypes.size(), bulkOnly);
            return caseTypes;
        }
    }

    CaseType getCaseTypeByShortCode(String shortCode){
        log.debug("Getting case type for short code {}", shortCode);
        CaseType caseType = caseTypeRepository.findByShortCode(shortCode);
        if(caseType != null) {
            log.info("Got CaseType {} for short code {}", caseType.getType(), shortCode);
            return caseType;
        } else {
            throw new ApplicationExceptions.EntityNotFoundException("CaseType not found for short code %s", shortCode);
        }
    }

    LocalDate getDeadlineForCaseType(String type, LocalDate receivedDate) {
        log.debug("Getting deadline for caseType {} with received date of {} ", type, receivedDate);
        CaseType caseType = getCaseType(type);
        int sla = stageTypeService.getStageType(caseType.getDeadlineStage()).getDeadline();
        Set<ExemptionDate> exemptions = holidayDateRepository.findAllByCaseType(caseType.getUuid());
        LocalDate deadline = Deadline.calculateDeadline(receivedDate, sla, exemptions);
        log.info("Got deadline ({}) for caseType {} with received date of {} ", deadline, type, receivedDate);
        return deadline;
    }

    Map<String, LocalDate> getAllStageDeadlinesForCaseType(String type, LocalDate receivedDate) {
        log.debug("Getting all stage deadlines for caseType {} with received date of {} ", type, receivedDate);
        CaseType caseType = getCaseType(type);
        Set<StageTypeEntity> stageTypes = stageTypeService.getAllStageTypesByCaseType(caseType.getUuid());
        Set<ExemptionDate> exemptions = holidayDateRepository.findAllByCaseType(caseType.getUuid());
        Map<String, LocalDate> deadlines = stageTypes.stream().filter(st -> st.getDeadline() > 0).collect(Collectors.toMap(StageTypeEntity::getType, stageType -> Deadline.calculateDeadline(receivedDate, stageType.getDeadline(), exemptions)));
        log.info("Got {} deadlines for caseType {} with received date of {} ", deadlines.size(), type, receivedDate);
        return deadlines;
    }

    CaseType getCaseTypeByUUID(UUID caseUUID) {
        String shortCode = caseUUID.toString().substring(34);
        log.debug("Looking up CaseType for Case: {} Shortcode: {}", caseUUID, shortCode);
        return getCaseTypeByShortCode(shortCode);
    }

    CaseType getCaseType(String type) {
        log.debug("Getting CaseType for type {}", type);
        CaseType caseType = caseTypeRepository.findByType(type);
        if (caseType != null) {
            log.info("Got CaseType {} for type {}", caseType.getUuid(), type);
            return caseType;
        } else {
            throw new ApplicationExceptions.EntityNotFoundException("CaseType for type %s was not found", type);
        }
    }
}
