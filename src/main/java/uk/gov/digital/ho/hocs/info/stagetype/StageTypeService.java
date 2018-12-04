package uk.gov.digital.ho.hocs.info.stagetype;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.digital.ho.hocs.info.RequestData;
import uk.gov.digital.ho.hocs.info.entities.StageTypeEntity;
import uk.gov.digital.ho.hocs.info.repositories.StageTypeRepository;

import java.util.Set;

@Service
@Slf4j
public class StageTypeService {

    private final StageTypeRepository stageTypeRepository;
    private final RequestData requestData;

    @Autowired
    public StageTypeService(StageTypeRepository stageTypeRepository, RequestData requestData) {
        this.stageTypeRepository = stageTypeRepository;
        this.requestData = requestData;
    }


    Set<StageTypeEntity> getAllStageTypes() {
        log.debug("Requesting all case types");
        return stageTypeRepository.findAllBy();
    }

}
