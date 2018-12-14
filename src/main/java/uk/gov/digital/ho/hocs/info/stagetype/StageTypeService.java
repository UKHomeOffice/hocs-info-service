package uk.gov.digital.ho.hocs.info.stagetype;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.digital.ho.hocs.info.RequestData;
import uk.gov.digital.ho.hocs.info.dto.TeamDto;
import uk.gov.digital.ho.hocs.info.entities.StageTypeEntity;
import uk.gov.digital.ho.hocs.info.repositories.StageTypeRepository;

import java.util.Set;

@Service
@Slf4j
public class StageTypeService {

    private final StageTypeRepository stageTypeRepository;

    @Autowired
    public StageTypeService(StageTypeRepository stageTypeRepository) {
        this.stageTypeRepository = stageTypeRepository;
    }

    Set<StageTypeEntity> getAllStageTypes() {
        log.debug("Requesting all case types");
        return stageTypeRepository.findAllBy();
    }

    TeamDto getTeamForStageType(String stageType) {
        return TeamDto.fromWithoutPermissions(stageTypeRepository.findByType(stageType).getTeam());
    }

}
