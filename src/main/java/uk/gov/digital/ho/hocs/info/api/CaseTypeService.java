package uk.gov.digital.ho.hocs.info.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.digital.ho.hocs.info.api.dto.CaseTypeActionDto;
import uk.gov.digital.ho.hocs.info.api.dto.CreateCaseTypeDto;
import uk.gov.digital.ho.hocs.info.domain.exception.ApplicationExceptions;
import uk.gov.digital.ho.hocs.info.domain.model.*;
import uk.gov.digital.ho.hocs.info.domain.repository.CaseActionTypeRepository;
import uk.gov.digital.ho.hocs.info.domain.repository.CaseTypeRepository;
import uk.gov.digital.ho.hocs.info.domain.repository.DocumentTagRepository;
import uk.gov.digital.ho.hocs.info.domain.repository.HolidayDateRepository;
import uk.gov.digital.ho.hocs.info.security.UserPermissionsService;
import uk.gov.digital.ho.hocs.info.utils.DateUtils;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CaseTypeService {

    private final CaseTypeRepository caseTypeRepository;
    private final DocumentTagRepository documentTagRepository;
    private final HolidayDateRepository holidayDateRepository;
    private final CaseActionTypeRepository caseActionTypeRepository;
    private final StageTypeService stageTypeService;
    private final UserPermissionsService userPermissionsService;
    private final LocalDateWrapper localDateWrapper;

    @Autowired
    public CaseTypeService(CaseTypeRepository caseTypeRepository, DocumentTagRepository documentTagRepository,
                           HolidayDateRepository holidayDateRepository, CaseActionTypeRepository caseActionTypeRepository, StageTypeService stageTypeService,
                           UserPermissionsService userPermissionsService, LocalDateWrapper localDateWrapper) {
        this.caseTypeRepository = caseTypeRepository;
        this.documentTagRepository = documentTagRepository;
        this.holidayDateRepository = holidayDateRepository;
        this.caseActionTypeRepository = caseActionTypeRepository;
        this.stageTypeService = stageTypeService;
        this.userPermissionsService = userPermissionsService;
        this.localDateWrapper = localDateWrapper;
    }

    Set<CaseType> getAllCaseTypes(Boolean addCaseTypeWithPreviousType) {
        log.debug("Getting all CaseTypes with addCaseTypeWithPreviousType:{}", addCaseTypeWithPreviousType);
        Set<CaseType> caseTypes = caseTypeRepository.findByIncludePreviousCaseType(addCaseTypeWithPreviousType != null && addCaseTypeWithPreviousType);
        log.info("Got {} CaseTypes", caseTypes.size());
        return caseTypes;
    }

    Set<CaseType> getAllCaseTypesForUser(boolean bulkOnly, boolean initialCaseType) {
        log.debug("Getting case types by User, bulkOnly = {}", bulkOnly);
        Set<UUID> userTeams = userPermissionsService.getUserTeams();
        Set<String> teams = userTeams.stream().map(UUID::toString).collect(Collectors.toSet());
        log.debug("Finding case types for {} teams", teams);
        if (userTeams.isEmpty()) {
            log.warn("No Teams - Returning 0 CaseTypes");
            return new HashSet<>(0);
        } else {
            Set<CaseType> caseTypes;
            if (bulkOnly) {
                caseTypes = caseTypeRepository.findAllBulkCaseTypesByTeam(teams, initialCaseType);
            } else {
                caseTypes = caseTypeRepository.findAllCaseTypesByTeam(teams, initialCaseType);
            }
            log.info("Got {} CaseTypes (bulkOnly = {})", caseTypes.size(), bulkOnly);
            return caseTypes;
        }
    }

    CaseType getCaseTypeByShortCode(String shortCode) {
        log.debug("Getting case type for short code {}", shortCode);
        CaseType caseType = caseTypeRepository.findByShortCode(shortCode);
        if (caseType != null) {
            log.info("Got CaseType {} for short code {}", caseType.getType(), shortCode);
            return caseType;
        } else {
            throw new ApplicationExceptions.EntityNotFoundException("CaseType not found for short code %s", shortCode);
        }
    }

    LocalDate getDeadlineForCaseType(String type, LocalDate receivedDate, int days, int extensionDays) {
        log.debug("Getting deadline for caseType {} with received date of {} and Days of {}", type, receivedDate, days);
        CaseType caseType = getCaseType(type);
        int sla = (days > 0) ? days : stageTypeService.getStageType(caseType.getDeadlineStage()).getDeadline();

        sla += extensionDays;

        Set<ExemptionDate> exemptions = holidayDateRepository.findAllByCaseType(caseType.getUuid());
        LocalDate deadline = Deadline.calculateDeadline(receivedDate, null, sla, exemptions);
        log.info("Got deadline ({}) for caseType {} with received date of {} ", deadline, type, receivedDate);
        return deadline;
    }

    LocalDate getDeadlineWarningForCaseType(String type, LocalDate receivedDate, int days) {
        log.debug("Getting deadline for caseType {} with received date of {} and Days of {}", type, receivedDate, days);
        CaseType caseType = getCaseType(type);
        int sla = (days > 0) ? days : stageTypeService.getStageType(caseType.getDeadlineStage()).getDeadlineWarning();
        Set<ExemptionDate> exemptions = holidayDateRepository.findAllByCaseType(caseType.getUuid());
        LocalDate deadline = Deadline.calculateDeadline(receivedDate, null, sla, exemptions);
        log.info("Got deadline ({}) for caseType {} with received date of {} ", deadline, type, receivedDate);
        return deadline;
    }

    Map<String, LocalDate> getAllStageDeadlinesForCaseType(String type, LocalDate receivedDate) {
        log.debug("Getting all stage deadlines for caseType {} with received date of {} ", type, receivedDate);
        CaseType caseType = getCaseType(type);
        Set<StageTypeEntity> stageTypes = stageTypeService.getAllStageTypesByCaseType(caseType.getUuid());
        Set<ExemptionDate> exemptions = holidayDateRepository.findAllByCaseType(caseType.getUuid());
        Map<String, LocalDate> deadlines = stageTypes.stream().filter(st -> st.getDeadline() >= 0)
                .sorted(Comparator.comparingInt(StageTypeEntity::getDisplayStageOrder))
                .collect(Collectors.toMap(StageTypeEntity::getType, stageType -> Deadline.calculateDeadline(receivedDate, null, stageType.getDeadline(), exemptions), (oldValue, newValue) -> oldValue, LinkedHashMap::new));
        log.info("Got {} deadlines for caseType {} with received date of {} ", deadlines.size(), type, receivedDate);
        return deadlines;
    }

    List<String> getDocumentTagsForCaseType(String caseType) {
        log.debug("Getting all document tags for caseType {}", caseType);
        List<DocumentTag> documentTags = documentTagRepository.findByCaseType(caseType);
        List<String> documentTagStrings = documentTags.stream().map(DocumentTag::getTag).collect(Collectors.toList());
        log.info("Got {} document tags for caseType {}", documentTagStrings.size(), caseType);
        return documentTagStrings;
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

    public int calculateWorkingDaysElapsedForCaseType(String caseType, LocalDate fromDate) {

        LocalDate now = localDateWrapper.now();
        if (fromDate == null || now.isBefore(fromDate) || now.isEqual(fromDate)) {
            return 0;
        }
        Set<LocalDate> exemptions = holidayDateRepository.findAllByCaseType(caseType).stream().map(ExemptionDate::getDate).collect(Collectors.toSet());
        LocalDate date = fromDate;
        int workingDays = 0;
        while (date.isBefore(now)) {
            if (!DateUtils.isDateNonWorkingDay(date, exemptions)) {
                workingDays++;
            }

            date = date.plusDays(1);
        }

        return workingDays;

    }

    public void createCaseType(CreateCaseTypeDto caseType) {

        caseTypeRepository.save(new CaseType(caseType.getDisplayName(),
                caseType.getShortCode(),
                caseType.getType(),
                caseType.getDeadlineStage(),
                caseType.isBulk(),
                caseType.isActive(),
                caseType.getPreviousCaseType()));
    }

    public List<CaseTypeActionDto> getCaseActionsByCaseType(String caseType) {
        log.debug("Received request for case actions with caseType {}", caseType);
        List<CaseTypeAction> caseActionEntities = caseActionTypeRepository.findAllByCaseTypeAndActiveIsTrue(caseType);
        log.info("Found {} case actions for caseType {}", caseActionEntities.size(), caseType);
        return caseActionEntities.stream().map(CaseTypeActionDto::from).collect(Collectors.toList());
    }

    public CaseTypeActionDto getCaseTypeActionById(UUID actionId) {
        log.debug("Request received for case type action id: {}", actionId);
        CaseTypeAction action = caseActionTypeRepository.findByUuid(actionId);
        if (action != null) {
            CaseTypeActionDto actionDto = CaseTypeActionDto.from(action);
            log.info("Found case type action {}", actionDto.toString());
            return actionDto;
        }
        throw new ApplicationExceptions.EntityNotFoundException(String.format("Case Type Action with id: %s, does not exist", actionId));
    }

    public List<CaseTypeActionDto> getAllCaseActions() {
        log.debug("Request received for all case type actions.");
        List<CaseTypeAction> caseTypeActionList = new LinkedList<>();
        Iterable<CaseTypeAction> caseTypeActionsItr = caseActionTypeRepository.findAll();
        caseTypeActionsItr.forEach(caseTypeActionList::add);
        log.info("Return list of {} case type actions.", caseTypeActionList.size());
        return caseTypeActionList.stream().map(CaseTypeActionDto::from).collect(Collectors.toList());
    }

    public Integer calculateRemainingDaysToDeadline(String caseType, LocalDate deadlineDate) {
        return Deadline.calculateRemainingWorkingDays(LocalDate.now(), deadlineDate, new HashSet<>(holidayDateRepository.findAllByCaseType(caseType)));
    }
}
