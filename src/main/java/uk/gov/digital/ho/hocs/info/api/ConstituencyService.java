package uk.gov.digital.ho.hocs.info.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.digital.ho.hocs.info.api.dto.CreateConstituencyDto;
import uk.gov.digital.ho.hocs.info.client.auditClient.AuditClient;
import uk.gov.digital.ho.hocs.info.client.caseworkclient.CaseworkClient;
import uk.gov.digital.ho.hocs.info.client.caseworkclient.dto.GetCaseworkCaseDataResponse;
import uk.gov.digital.ho.hocs.info.domain.exception.ApplicationExceptions;
import uk.gov.digital.ho.hocs.info.domain.model.Constituency;
import uk.gov.digital.ho.hocs.info.domain.repository.ConstituencyRepository;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class ConstituencyService {

    private final ConstituencyRepository constituencyRepository;
    private final CaseworkClient caseworkClient;
    private final AuditClient auditClient;

    @Autowired
    public ConstituencyService(ConstituencyRepository constituencyRepository,
                        CaseworkClient caseworkClient,
                        AuditClient auditClient) {
        this.constituencyRepository = constituencyRepository;
        this.caseworkClient = caseworkClient;
        this.auditClient = auditClient;
    }

    public Constituency getConstituency(UUID constituencyUUID) {
        log.debug("Requesting constituency uuid {}", constituencyUUID);
        return constituencyRepository.findConstituencyByUUID(constituencyUUID);
    }

    public Constituency getConstituencyByMemberExternalReference(String externalReference) {
        log.debug("Requesting constituency by member externalReference {}", externalReference);
        return constituencyRepository.findConstituencyByMemberExternalReference(externalReference);
    }

    public List<Constituency> getConstituencyList(String caseType) {
        log.debug("Requesting all constituencies for {}", caseType);
        return constituencyRepository.findAllActiveConstituencyByCaseType(caseType);
    }

    public List<Constituency> getConstituencyList(UUID caseUUID) {
        GetCaseworkCaseDataResponse caseTypeResponse = caseworkClient.getCase(caseUUID);
        return getConstituencyList(caseTypeResponse.getType());
    }

    public UUID createConstituency(CreateConstituencyDto request) {
        String displayName = request.getConstituencyName();
        log.debug("Creating constituency: {}", displayName);
        Constituency existingConstituency = constituencyRepository.findConstituencyByName(displayName);
        if (existingConstituency == null) {
            Constituency constituency = new Constituency(displayName);
            constituencyRepository.save(constituency);
            log.info("Created constituency: {}", displayName);
            auditClient.createConstituencyAudit(constituency);
            return constituency.getUuid();
        } else {
            if (existingConstituency.isActive() == true) {
                log.debug(
                        "Unable to create constituency, active constituency with this name already exists");
            } else {
                log.debug(
                        "Unable to create constituency, inactive constituency with this name already exists");
            }
            throw new ApplicationExceptions.ConstituencyCreationException(
                    "Constituency already exists with this name");
        }
    }

    public void deleteConstituency(UUID constituencyUUID) {

        log.debug("Deleting constituency: {}", constituencyUUID);
        Constituency constituency = constituencyRepository.findConstituencyByUUID(constituencyUUID);
        if (constituency == null){
            log.debug("Unable to delete  constituency, UUID: {} does not exist", constituencyUUID);
            throw new ApplicationExceptions.EntityNotFoundException
                    ("Unable to delete constituency, UUID: {} does not exist", constituencyUUID);
        } else {
            setConstituencyToInactive(constituency);
        }
    }

    public void setConstituencyToInactive(Constituency constituency){
        constituency.setActive(false);
        constituencyRepository.save(constituency);
        log.info("Deleted constituency: {}", constituency.getConstituencyName());
        auditClient.deleteConstituencyAudit(constituency);
    }

    public void reactivateConstituency(UUID constituencyUUID) {

        log.debug("Reactivating constituency: {}", constituencyUUID);
        Constituency constituency = constituencyRepository.findConstituencyByUUID(constituencyUUID);
        if (constituency == null){
            throw new ApplicationExceptions.EntityNotFoundException("Constituency with UUID {} does not exist", constituencyUUID.toString());
        }
        constituency.setActive(true);
        constituencyRepository.save(constituency);
        log.info("Activated constituency: {}", constituencyUUID);
        auditClient.reactivateConstituencyAudit(constituency);
    }

    public List<Constituency> getConstituencys() {
        log.debug("Requesting all constituencys");
        return constituencyRepository.findAllBy();
    }
}
