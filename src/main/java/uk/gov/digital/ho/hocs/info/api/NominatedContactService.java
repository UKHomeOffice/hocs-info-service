package uk.gov.digital.ho.hocs.info.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import uk.gov.digital.ho.hocs.info.domain.exception.ApplicationExceptions;
import uk.gov.digital.ho.hocs.info.domain.model.NominatedContact;
import uk.gov.digital.ho.hocs.info.domain.repository.NominatedContactRepository;

import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
public class NominatedContactService {

    private final NominatedContactRepository nominatedContactRepository;

    @Autowired
    public NominatedContactService(NominatedContactRepository nominatedContactRepository) {
        this.nominatedContactRepository = nominatedContactRepository;
    }

    public Set<NominatedContact> getNominatedContacts(UUID teamUUID) {
        log.debug("Getting nominated contacts for team UUID {} ", teamUUID);
        Set<NominatedContact> contacts = nominatedContactRepository.findAllByTeamUUID(teamUUID);
        log.info("Retrieved nominated contacts for team UUID {} ", teamUUID);
        return contacts;
    }

    public NominatedContact createNominatedContact(UUID teamUUID, String emailAddress){
        log.debug("Creating new nominated contact for Team: {} with Email: {}", teamUUID, emailAddress);
        NominatedContact nominatedContact = new NominatedContact(teamUUID, emailAddress);
        try {
            nominatedContactRepository.save(nominatedContact);
        }
        catch (DataIntegrityViolationException e) {
            throw new ApplicationExceptions.EntityAlreadyExistsException("Nominated contact already exists");
        }
        log.info("Created nominated contact for team UUID: {} with email: {}", teamUUID, emailAddress);
        return nominatedContact;
    }

    public void updateNominatedContact(UUID nominatedContactUUID, String emailAddress){
        log.debug("Updating nominated contact uuid: {} with email: {}", nominatedContactUUID, emailAddress);
        NominatedContact nominatedContact = nominatedContactRepository.findByUuid(nominatedContactUUID);
        nominatedContact.setEmailAddress(emailAddress);
        nominatedContactRepository.save(nominatedContact);
        log.info("Updated nominated contact uuid: {} with email:{} ", nominatedContactUUID, emailAddress);
    }

    public void deleteNominatedContact(UUID teamUUID, UUID nominatedContactUUID){
        log.debug("Deleting nominated contact with uuid: {} from team {}", nominatedContactUUID, teamUUID);

        NominatedContact nominatedContact = nominatedContactRepository.findByUuid(nominatedContactUUID);
        nominatedContactRepository.delete(nominatedContact);
        log.info("Deleted nominated contact uuid: {} ", nominatedContactUUID);
    }
}
