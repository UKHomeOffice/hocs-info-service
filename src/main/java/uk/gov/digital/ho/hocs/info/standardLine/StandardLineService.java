package uk.gov.digital.ho.hocs.info.standardLine;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.digital.ho.hocs.info.RequestData;
import uk.gov.digital.ho.hocs.info.casetype.CaseTypeService;
import uk.gov.digital.ho.hocs.info.entities.StandardLine;
import uk.gov.digital.ho.hocs.info.exception.EntityPermissionException;
import uk.gov.digital.ho.hocs.info.repositories.StandardLineRepository;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class StandardLineService {

    private final StandardLineRepository standardLineRepository;
    private final CaseTypeService caseTypeService;
    private final RequestData requestData;

    @Autowired
    public StandardLineService(StandardLineRepository standardLineRepository, CaseTypeService caseTypeService, RequestData requestData) {
        this.standardLineRepository = standardLineRepository;
        this.caseTypeService = caseTypeService;
        this.requestData = requestData;
    }

    public List<StandardLine> getStandardLines(String caseType, UUID topicUUID) throws EntityPermissionException {
        log.info("Requesting Standard Lines for Topic {} ", topicUUID);
        if (caseTypeService.hasPermissionForCaseType(caseType)) {
            return standardLineRepository.findStandardLinesByCaseTopic(topicUUID);
        } else {
            //TODO AUDIT permission exception
            throw new EntityPermissionException("Not allowed to get Units for CaseType, CaseType: %s not in Roles: %s", caseType, requestData.rolesString());
        }
    }

    public StandardLine getStandardLineKey(UUID standardLineUUID) {
        log.info("Requesting standard line key for template {} ", standardLineUUID);
        return standardLineRepository.findStandardLineByUuid(standardLineUUID);
    }
}
