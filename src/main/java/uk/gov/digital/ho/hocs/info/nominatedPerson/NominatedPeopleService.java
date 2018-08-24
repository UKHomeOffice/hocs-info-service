package uk.gov.digital.ho.hocs.info.nominatedPerson;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.digital.ho.hocs.info.entities.NominatedPerson;
import uk.gov.digital.ho.hocs.info.repositories.NominatedPersonRepository;

import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
public class NominatedPeopleService {

    private final NominatedPersonRepository nominatedPersonRepository;

    @Autowired
    public NominatedPeopleService(NominatedPersonRepository nominatedPersonRepository) {
        this.nominatedPersonRepository = nominatedPersonRepository;
    }

    public Set<NominatedPerson> getNominatedPerson(UUID teamUUID) {
        log.info("Requesting nominated person(s) for team UUID {} ", teamUUID);

        return nominatedPersonRepository.findAllByTeamUUID(teamUUID);
    }
}
