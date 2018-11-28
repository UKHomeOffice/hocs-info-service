package uk.gov.digital.ho.hocs.info.casetype;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.digital.ho.hocs.info.RequestData;
import java.util.Set;
import uk.gov.digital.ho.hocs.info.entities.CaseTypeEntity;
import uk.gov.digital.ho.hocs.info.repositories.CaseTypeRepository;



@Service
@Slf4j
public class CaseTypeService {

    private final CaseTypeRepository caseTypeRepository;
    private final RequestData requestData;

    @Autowired
    public CaseTypeService(CaseTypeRepository caseTypeRepository, RequestData requestData) {
        this.caseTypeRepository = caseTypeRepository;
        this.requestData = requestData;
    }

    Set<CaseTypeEntity> getCaseTypes() {
        log.debug("Requesting all case types");
        return caseTypeRepository.findAllCaseTypesByTenant(requestData.roles());
    }

    Set<CaseTypeEntity> getCaseTypesBulk() {
        log.debug("Requesting all case types for Bulk upload");
        return caseTypeRepository.findAllBulkCaseTypesByTenant(requestData.roles());
    }


}
