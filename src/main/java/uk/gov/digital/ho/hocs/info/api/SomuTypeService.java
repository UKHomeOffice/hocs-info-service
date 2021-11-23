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
        Set<SomuType> somuTypes = somuTypeRepository.findAll();
        log.info("Got all {} SomuTypes", somuTypes.size());
        return somuTypes;
    }

    Set<SomuType> getAllSomuTypesForCaseType(String caseType) {
        log.debug("Getting all SomuTypes for case type: {}", caseType);
        Set<SomuType> somuTypes = somuTypeRepository.findAllByCaseType(caseType);
        log.info("Got all {} SomuTypes for case type: {}", somuTypes.size(), caseType);
        return somuTypes;
    }

    SomuType getSomuTypeForCaseTypeAndType(String caseType, String type) {
        log.debug("Getting SomuTypes for Case Type {} and Type {}", caseType, type);
        SomuType somuType = somuTypeRepository.findByCaseTypeAndType(caseType, type);
        log.info("Got SomuType {} for Case Type {} and Type {}", somuType.getUuid(), caseType, type);
        return somuType;
    }

    SomuType upsertSomuTypeForCaseTypeAndType(String caseType, String type, String schema, boolean active) {
        log.debug("Upserting SomuTypes for Case Type {} and Type {}", caseType, type);
        SomuType somuType = somuTypeRepository.findByCaseTypeAndType(caseType, type);
        if (somuType == null) {
            somuType = new SomuType(caseType, type, schema, active);
            somuTypeRepository.save(somuType);
            log.info("Inserted SomuType {} for Case Type {} and Type {}", somuType.getUuid(), caseType, type);
        } else {
            somuType.setSchema(schema);
            somuType.setActive(active);
            somuTypeRepository.save(somuType);
            log.info("Updated SomuType {} for Case Type {} and Type {}", somuType.getUuid(), caseType, type);
        }
        return somuType;
    }
}
