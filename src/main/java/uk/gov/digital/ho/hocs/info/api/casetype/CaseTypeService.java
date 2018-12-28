package uk.gov.digital.ho.hocs.info.api.casetype;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import uk.gov.digital.ho.hocs.info.domain.model.CaseTypeEntity;
import uk.gov.digital.ho.hocs.info.domain.repository.CaseTypeRepository;
import uk.gov.digital.ho.hocs.info.security.UserPermissionsService;


@Service
@Slf4j
public class CaseTypeService {

    private final CaseTypeRepository caseTypeRepository;
    private final UserPermissionsService userPermissionsService;
    @Autowired
    public CaseTypeService(CaseTypeRepository caseTypeRepository, UserPermissionsService userPermissionsService) {
        this.caseTypeRepository = caseTypeRepository;
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
