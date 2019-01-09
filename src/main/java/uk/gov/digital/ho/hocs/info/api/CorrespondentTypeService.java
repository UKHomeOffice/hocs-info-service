package uk.gov.digital.ho.hocs.info.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.digital.ho.hocs.info.domain.model.CorrespondentType;
import uk.gov.digital.ho.hocs.info.domain.repository.CorrespondentTypeRepository;

import java.util.Set;

@Service
@Slf4j
public class CorrespondentTypeService {

    private final CorrespondentTypeRepository correspondentTypeRepository;

    @Autowired
    public CorrespondentTypeService(CorrespondentTypeRepository correspondentTypeRepository) {
        this.correspondentTypeRepository = correspondentTypeRepository;
    }

    Set<CorrespondentType> getAllCorrespondentTypes() {
        log.debug("Getting all correspondent types");
        Set<CorrespondentType> correspondentTypes = correspondentTypeRepository.findAll();
        log.info("Got {} correspondent types", correspondentTypes.size());
        return correspondentTypes;
    }
}
