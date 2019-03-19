package uk.gov.digital.ho.hocs.info.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.digital.ho.hocs.info.client.auditClient.AuditClient;
import uk.gov.digital.ho.hocs.info.domain.model.CorrespondentType;
import uk.gov.digital.ho.hocs.info.domain.repository.CorrespondentTypeRepository;

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

    public CorrespondentType createCorrespondentType(String displayName, String type) {
        log.debug("Creating Correspondent Type with Display name: {}", displayName);
        CorrespondentType correspondentType = new CorrespondentType(displayName, type);
        correspondentTypeRepository.save(correspondentType);
        log.info("Created Correspondent Type with Display name: {}", displayName);
        auditClient.createCorrespondentType(correspondentType);
        return correspondentType;
    }
}
