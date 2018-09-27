package uk.gov.digital.ho.hocs.info.correspondentType;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.digital.ho.hocs.info.entities.CorrespondentType;
import uk.gov.digital.ho.hocs.info.repositories.CorrespondentTypeRepository;

import java.util.Set;

@Service
@Slf4j
public class CorrespondentTypeService {

    private final CorrespondentTypeRepository correspondentTypeRepository;

    @Autowired
    public CorrespondentTypeService(CorrespondentTypeRepository correspondentTypeRepository) {
        this.correspondentTypeRepository = correspondentTypeRepository;
    }

    public Set<CorrespondentType> getCorrespondentTypes() {
        log.debug("Requesting all correspondent types");
        return correspondentTypeRepository.findAll();
    }
}
