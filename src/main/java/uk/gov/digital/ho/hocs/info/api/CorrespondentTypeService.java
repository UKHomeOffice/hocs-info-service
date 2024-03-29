package uk.gov.digital.ho.hocs.info.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import uk.gov.digital.ho.hocs.info.client.audit.client.AuditClient;
import uk.gov.digital.ho.hocs.info.domain.exception.ApplicationExceptions;
import uk.gov.digital.ho.hocs.info.domain.model.CorrespondentType;
import uk.gov.digital.ho.hocs.info.domain.repository.CorrespondentTypeRepository;

import java.util.Optional;
import java.util.Set;

@Service
@Slf4j
public class CorrespondentTypeService {

    private final CorrespondentTypeRepository correspondentTypeRepository;

    private final AuditClient auditClient;

    @Autowired
    public CorrespondentTypeService(CorrespondentTypeRepository correspondentTypeRepository, AuditClient auditClient) {
        this.correspondentTypeRepository = correspondentTypeRepository;
        this.auditClient = auditClient;
    }

    Set<CorrespondentType> getAllCorrespondentTypes() {
        log.debug("Getting all correspondent types");
        Set<CorrespondentType> correspondentTypes = correspondentTypeRepository.findAll();
        log.info("Got {} correspondent types", correspondentTypes.size());
        return correspondentTypes;
    }

    Set<CorrespondentType> getCorrespondentTypesByCaseType(String caseType) {
        log.debug("Getting all correspondent types for Case Type {}", caseType);
        Set<CorrespondentType> correspondentTypes = correspondentTypeRepository.findAllByCaseType(caseType);
        log.info("Got {} correspondent types for Case Type {}", correspondentTypes.size(), caseType);
        return correspondentTypes;
    }

    Set<CorrespondentType> getSelectableCorrespondentTypesByCaseType(String caseType) {
        log.debug("Getting all selectable correspondent types for Case Type {}", caseType);
        Set<CorrespondentType> correspondentTypes = correspondentTypeRepository.findAllSelectableByCaseType(caseType);
        log.info("Got {} correspondent types for Case Type {}", correspondentTypes.size(), caseType);
        return correspondentTypes;
    }

    public CorrespondentType createCorrespondentType(String displayName, String type) {
        log.debug("Creating Correspondent Type with Display name: {}", displayName);
        validateInput(displayName, type);
        CorrespondentType correspondentType = new CorrespondentType(displayName, type);
        correspondentTypeRepository.save(correspondentType);
        log.info("Created Correspondent Type with Display name: {}", displayName);
        auditClient.createCorrespondentType(correspondentType);
        return correspondentType;
    }

    private void validateInput(String displayName, String type) {
        if (!StringUtils.hasText(displayName)) {
            throw new ApplicationExceptions.EntityCreationException(
                "Cannot create Correspondent Type of Type: %s, with no display name", type);
        }
        if (!StringUtils.hasText(type)) {
            throw new ApplicationExceptions.EntityCreationException(
                "Cannot create Correspondent Type of Display Name: %s, with no type", displayName);
        }
    }

}
