package uk.gov.digital.ho.hocs.info.casetype;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.digital.ho.hocs.info.RequestData;
import java.util.Set;
import uk.gov.digital.ho.hocs.info.entities.CaseTypeEntity;
import uk.gov.digital.ho.hocs.info.repositories.CaseTypeRepository;
import uk.gov.digital.ho.hocs.info.security.UserPermissionsService;
import uk.gov.digital.ho.hocs.info.user.UserService;


@Service
@Slf4j
public class CaseTypeService {

    private final CaseTypeRepository caseTypeRepository;
    private final RequestData requestData;
private final UserPermissionsService userPermissionsService;
    @Autowired
    public CaseTypeService(CaseTypeRepository caseTypeRepository, RequestData requestData, UserPermissionsService userPermissionsService) {
        this.caseTypeRepository = caseTypeRepository;
        this.requestData = requestData;
        this.userPermissionsService = userPermissionsService;
    }

    CaseTypeEntity getCaseTypeByShortCode(String shortcode){
        return caseTypeRepository.findByShortCode(shortcode);

    }

    Set<CaseTypeEntity> getAllCaseTypes() {
        log.debug("Requesting all case types");
        return caseTypeRepository.findAllBy();
    }

    Set<CaseTypeEntity> getCaseTypes() {
        log.debug("Requesting all case types for single create");
        return caseTypeRepository.findAllCaseTypesByTeam(userPermissionsService.getUserTeams());
    }

    Set<CaseTypeEntity> getCaseTypesBulk() {
        log.debug("Requesting all case types for Bulk upload");
        return caseTypeRepository.findAllBulkCaseTypesByTeam(userPermissionsService.getUserTeams());
    }


}
