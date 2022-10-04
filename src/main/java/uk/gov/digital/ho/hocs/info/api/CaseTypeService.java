package uk.gov.digital.ho.hocs.info.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.digital.ho.hocs.info.api.dto.CaseTypeActionDto;
import uk.gov.digital.ho.hocs.info.api.dto.CreateCaseTypeDto;
import uk.gov.digital.ho.hocs.info.domain.exception.ApplicationExceptions;
import uk.gov.digital.ho.hocs.info.domain.model.CaseType;
import uk.gov.digital.ho.hocs.info.domain.model.CaseTypeAction;
import uk.gov.digital.ho.hocs.info.domain.model.DocumentTag;
import uk.gov.digital.ho.hocs.info.domain.model.ExemptionDate;
import uk.gov.digital.ho.hocs.info.domain.repository.CaseActionTypeRepository;
import uk.gov.digital.ho.hocs.info.domain.repository.CaseTypeRepository;
import uk.gov.digital.ho.hocs.info.domain.repository.DocumentTagRepository;
import uk.gov.digital.ho.hocs.info.domain.repository.HolidayDateRepository;
import uk.gov.digital.ho.hocs.info.security.UserPermissionsService;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CaseTypeService {

    private final CaseTypeRepository caseTypeRepository;

    private final DocumentTagRepository documentTagRepository;

    private final HolidayDateRepository holidayDateRepository;

    private final CaseActionTypeRepository caseActionTypeRepository;

    private final UserPermissionsService userPermissionsService;

    @Autowired
    public CaseTypeService(CaseTypeRepository caseTypeRepository,
                           DocumentTagRepository documentTagRepository,
                           HolidayDateRepository holidayDateRepository,
                           CaseActionTypeRepository caseActionTypeRepository,
                           UserPermissionsService userPermissionsService) {
        this.caseTypeRepository = caseTypeRepository;
        this.documentTagRepository = documentTagRepository;
        this.holidayDateRepository = holidayDateRepository;
        this.caseActionTypeRepository = caseActionTypeRepository;
        this.userPermissionsService = userPermissionsService;
    }

    Set<CaseType> getAllCaseTypes(Boolean addCaseTypeWithPreviousType) {
        log.debug("Getting all CaseTypes with addCaseTypeWithPreviousType:{}", addCaseTypeWithPreviousType);
        Set<CaseType> caseTypes = caseTypeRepository.findByIncludePreviousCaseType(
            addCaseTypeWithPreviousType != null && addCaseTypeWithPreviousType);
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

    @Deprecated(forRemoval = true)
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

    public void createCaseType(CreateCaseTypeDto caseType) {

        caseTypeRepository.save(new CaseType(caseType.getDisplayName(), caseType.getShortCode(), caseType.getType(),
            caseType.getDeadlineStage(), caseType.isBulk(), caseType.isActive(), caseType.getPreviousCaseType()));
    }

    public List<CaseTypeActionDto> getCaseActionsByCaseType(String caseType) {
        log.debug("Received request for case actions with caseType {}", caseType);
        List<CaseTypeAction> caseActionEntities = caseActionTypeRepository.findAllByCaseTypeAndActiveIsTrue(caseType);
        log.info("Found {} case actions for caseType {}", caseActionEntities.size(), caseType);
        return caseActionEntities.stream().map(CaseTypeActionDto::from).collect(Collectors.toList());
    }

    public Set<LocalDate> getExemptionDatesByCaseType(String caseType) {
        log.debug("Received request for case exemption dates with caseType {}", caseType);
        final List<ExemptionDate> exemptionDates = holidayDateRepository.findAllByCaseType(caseType);
        log.info("Found {} case exemption dates for caseType {}", exemptionDates.size(), caseType);
        return exemptionDates.stream().map(ExemptionDate::getDate).collect(Collectors.toSet());
    }

    public CaseTypeActionDto getCaseTypeActionById(UUID actionId) {
        log.debug("Request received for case type action id: {}", actionId);
        CaseTypeAction action = caseActionTypeRepository.findByUuid(actionId);
        if (action != null) {
            CaseTypeActionDto actionDto = CaseTypeActionDto.from(action);
            log.info("Found case type action {}", actionDto.toString());
            return actionDto;
        }
        throw new ApplicationExceptions.EntityNotFoundException(
            String.format("Case Type Action with id: %s, does not exist", actionId));
    }

    public List<CaseTypeActionDto> getAllCaseActions() {
        log.debug("Request received for all case type actions.");
        List<CaseTypeAction> caseTypeActionList = new LinkedList<>();
        Iterable<CaseTypeAction> caseTypeActionsItr = caseActionTypeRepository.findAll();
        caseTypeActionsItr.forEach(caseTypeActionList::add);
        log.info("Return list of {} case type actions.", caseTypeActionList.size());
        return caseTypeActionList.stream().map(CaseTypeActionDto::from).collect(Collectors.toList());
    }

}
