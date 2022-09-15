package uk.gov.digital.ho.hocs.info.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;

import uk.gov.digital.ho.hocs.info.domain.exception.ApplicationExceptions;
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

    Boolean getCanDisplayContributionsForStageType(String type) {
        log.debug("Getting Can Display Contributions for StageType type {}", type);
        Boolean canDisplayContributions = stageTypeRepository.findByType(type).isCanDisplayContributions();
        log.info("Got Can Display Contributions as {} for stageType {}", canDisplayContributions, type);
        return canDisplayContributions;
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
        if (stageType != null) {
            log.info("Got StageType {} for type {}", stageType.getUuid(), type);
            return stageType;
        } else {
            throw new ApplicationExceptions.EntityNotFoundException("StageType for type %s was not found", type);
        }
    }

}
