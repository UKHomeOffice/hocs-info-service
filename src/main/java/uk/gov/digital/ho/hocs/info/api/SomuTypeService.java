package uk.gov.digital.ho.hocs.info.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.digital.ho.hocs.info.domain.exception.ApplicationExceptions;
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

    SomuType upsertSomuTypeForCaseTypeAndType(String caseType, String type, String schema) {
        log.debug("Upserting SomuTypes for Case Type {} and Type {}", caseType, type);
        SomuType somuType = somuTypeRepository.findByCaseTypeAndType(caseType, type);
        if (somuType == null) {
        somuType = new SomuType(caseType, type, schema, true);
            somuTypeRepository.save(somuType);
            log.info("Inserted SomuType {} for Case Type {} and Type {}", somuType.getUuid(), caseType, type);
        } else {
            somuType.setSchema(schema);
            somuTypeRepository.save(somuType);
            log.info("Updated SomuType {} for Case Type {} and Type {}", somuType.getUuid(), caseType, type);
        }
        return somuType;
    }

    void deleteSomuTypeForCaseTypeAndType(String caseType, String type) {
        log.debug("Deleting SomuTypes for Case Type {} and Type {}", caseType, type);
        SomuType somuType = somuTypeRepository.findByCaseTypeAndType(caseType, type);
        if (somuType == null) {
            throw new ApplicationExceptions.EntityNotFoundException(
                    String.format("SomuType {}/{} not found", caseType, type));
        } else {
            somuType.delete();
            somuTypeRepository.save(somuType);
        }
        log.info("Deleted SomuType {} for Case Type {} and Type {}", somuType.getUuid(), caseType, type);
        return;
    }
}
