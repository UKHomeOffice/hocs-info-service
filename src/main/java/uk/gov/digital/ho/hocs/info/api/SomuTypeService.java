package uk.gov.digital.ho.hocs.info.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.digital.ho.hocs.info.domain.model.SomuType;
import uk.gov.digital.ho.hocs.info.domain.repository.SomuTypeRepository;

import java.util.Set;

@Service
@Slf4j
public class SomuTypeService {

    private final SomuTypeRepository somuTypeRepository;

    @Autowired
    public SomuTypeService(SomuTypeRepository somuTypeRepository) {
        this.somuTypeRepository = somuTypeRepository;
    }

    Set<SomuType> getAllSomuTypes() {
        log.debug("Getting all SomuTypes");
        Set<SomuType> somuTypes = somuTypeRepository.findAllBy();
        log.info("Got {} SomuTypes", somuTypes.size());
        return somuTypes;
    }

    SomuType getSomuTypeForCaseTypeAndType(String caseType, String type) {
        log.debug("Getting SomuTypes for Case Type {} and Type {}", caseType, type);
        SomuType somuType = somuTypeRepository.findByCaseTypeAndType(caseType, type);
        log.info("Got SomuType {} for Case Type {} and Type {}", somuType.getUuid(), caseType, type);
        return somuType;
    }
}
