package uk.gov.digital.ho.hocs.info.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

import uk.gov.digital.ho.hocs.info.domain.exception.ApplicationExceptions;
import uk.gov.digital.ho.hocs.info.domain.model.Deadline;
import uk.gov.digital.ho.hocs.info.domain.model.ExemptionDate;
import uk.gov.digital.ho.hocs.info.domain.model.StageTypeEntity;
import uk.gov.digital.ho.hocs.info.domain.model.Team;
import uk.gov.digital.ho.hocs.info.domain.repository.HolidayDateRepository;
import uk.gov.digital.ho.hocs.info.domain.repository.StageTypeRepository;

@Service
@Slf4j
public class StageTypeService {

    private final StageTypeRepository stageTypeRepository;
    private final HolidayDateRepository holidayDateRepository;

    @Autowired
    public StageTypeService(StageTypeRepository stageTypeRepository, HolidayDateRepository holidayDateRepository) {
        this.stageTypeRepository = stageTypeRepository;
        this.holidayDateRepository = holidayDateRepository;
    }

    Set<StageTypeEntity> getAllStageTypes() {
        log.debug("Getting all StageTypes");
        Set<StageTypeEntity> stageTypes = stageTypeRepository.findAllBy();
        log.info("Got {} StageTypes", stageTypes.size());
        return stageTypes;
    }

    Team getTeamForStageType(String stageType) {
        log.debug("Getting Team for StageType {}", stageType);
        Team team = getStageType(stageType).getTeam();
        log.info("Got Team {} for stageType {}", team.getUuid(), stageType);
        return team;
    }

    Boolean getContributionsForStageType(String type) {
        log.debug("Getting Contributions for StageType type {}", type);
        Boolean contributions = stageTypeRepository.findByType(type).isContributions();
        log.info("Got Contributions as {} for stageType {}", contributions, type);
        return contributions;
    }

    LocalDate getDeadlineForStageType(String stageType, LocalDate receivedDate, LocalDate caseDeadline) {
        log.debug("Getting deadline for stageType {} with received date of {} ", stageType, receivedDate);
        Set<ExemptionDate> holidays = holidayDateRepository.findAllByStageType(stageType);
        int sla = getStageType(stageType).getDeadline();
        Deadline deadline = new Deadline(receivedDate, caseDeadline, sla, holidays);
        log.info("Got deadline ({}) for stageType {} with received date of {} ", deadline.getDate(), stageType, receivedDate);
        return deadline.getDate();
    }

    LocalDate getDeadlineWarningForStageType(String stageType, LocalDate receivedDate, LocalDate caseDeadlineWarning) {
        log.debug("Getting deadline warning for stageType {} with received date of {} ", stageType, receivedDate);
        Set<ExemptionDate> holidays = holidayDateRepository.findAllByStageType(stageType);
        int sla = getStageType(stageType).getDeadlineWarning();
        Deadline deadline = new Deadline(receivedDate, caseDeadlineWarning, sla, holidays);
        log.info("Got deadline warning ({}) for stageType {} with received date of {} ", deadline.getDate(), stageType, receivedDate);
        return deadline.getDate();
    }

    Set<StageTypeEntity> getAllStageTypesByCaseType(UUID caseTypeUUID) {
        log.debug("Getting all StageTypes for caseType {}", caseTypeUUID);
        Set<StageTypeEntity> stageTypes = stageTypeRepository.findAllByCaseTypeUUID(caseTypeUUID);
        log.info("Got {} StageTypes for caseType", stageTypes.size(), caseTypeUUID);
        return stageTypes;
    }

     StageTypeEntity getStageType(String type) {
         log.debug("Getting StageType for type {}", type);
         StageTypeEntity stageType = stageTypeRepository.findByType(type);
        if(stageType != null) {
            log.info("Got StageType {} for type {}", stageType.getUuid(), type);
            return stageType;
        } else{
            throw new ApplicationExceptions.EntityNotFoundException("StageType for type %s was not found", type);
        }
    }
}
