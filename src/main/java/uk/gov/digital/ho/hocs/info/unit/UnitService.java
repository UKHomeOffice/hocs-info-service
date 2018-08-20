package uk.gov.digital.ho.hocs.info.unit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.digital.ho.hocs.info.RequestData;
import uk.gov.digital.ho.hocs.info.casetype.CaseTypeService;
import uk.gov.digital.ho.hocs.info.entities.Unit;
import uk.gov.digital.ho.hocs.info.exception.EntityPermissionException;
import uk.gov.digital.ho.hocs.info.repositories.UnitRepository;

import java.util.Set;

@Service
@Slf4j
public class UnitService {

    private final UnitRepository unitRepository;
    private final CaseTypeService caseTypeService;
    private final RequestData requestData;

    @Autowired
    public UnitService(UnitRepository unitRepository, CaseTypeService caseTypeService, RequestData requestData) {
        this.unitRepository = unitRepository;
        this.caseTypeService = caseTypeService;
        this.requestData = requestData;
    }

    public Set<Unit> getActiveUnitsByCaseType(String caseType) throws EntityPermissionException {
        log.debug("Requesting all Members for CaseType {}", caseType);
        if (caseTypeService.hasPermissionForCaseType(caseType)) {
            return unitRepository.findAllActiveUnitsByCaseType(caseType);
        } else {
            throw new EntityPermissionException("Not allowed to get Units for CaseType, CaseType: %s not in Roles: %s", caseType, requestData.rolesString());
        }
    }
}

