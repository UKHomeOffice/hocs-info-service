package uk.gov.digital.ho.hocs.info.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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

    public Set<NominatedContact> getNominatedContact(UUID teamUUID) {
        log.debug("Getting nominated contacts for team UUID {} ", teamUUID);
        Set<NominatedContact> contacts = nominatedContactRepository.findAllByTeamUUID(teamUUID);
        log.info("Got {} nominated contacts for team UUID {} ", contacts.size(), teamUUID);
        return contacts;
    }
}
