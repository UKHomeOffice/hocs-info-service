package uk.gov.digital.ho.hocs.info.standardLines;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.digital.ho.hocs.info.RequestData;
import uk.gov.digital.ho.hocs.info.casetype.CaseTypeService;
import uk.gov.digital.ho.hocs.info.entities.StandardLines;
import uk.gov.digital.ho.hocs.info.exception.EntityPermissionException;
import uk.gov.digital.ho.hocs.info.repositories.StandardLinesRepository;

import java.util.UUID;

@Service
@Slf4j
public class StandardLinesService {

    private final StandardLinesRepository standardLinesRepository;
    private final CaseTypeService caseTypeService;
    private final RequestData requestData;

    @Autowired
    public StandardLinesService(StandardLinesRepository standardLinesRepository, CaseTypeService caseTypeService, RequestData requestData) {
        this.standardLinesRepository = standardLinesRepository;
        this.caseTypeService = caseTypeService;
        this.requestData = requestData;
    }

    public StandardLines getStandardLines(String caseType, UUID topicUUID) throws EntityPermissionException {
        log.info("Requesting Standard Lines for Topic {} ", topicUUID);
        if (caseTypeService.hasPermissionForCaseType(caseType)) {
            return standardLinesRepository.findStandardLinesByCaseTopic(topicUUID);
        } else {
            //TODO AUDIT permission exception
            throw new EntityPermissionException("Not allowed to get Units for CaseType, CaseType: %s not in Roles: %s", caseType, requestData.rolesString());
        }
    }
}
