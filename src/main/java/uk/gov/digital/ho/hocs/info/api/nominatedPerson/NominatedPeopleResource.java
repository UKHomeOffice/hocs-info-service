package uk.gov.digital.ho.hocs.info.api.nominatedPerson;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.digital.ho.hocs.info.api.dto.GetNominatedPeopleResponse;
import uk.gov.digital.ho.hocs.info.domain.model.NominatedPerson;

import java.util.Set;
import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
public class NominatedPeopleResource {

    private final NominatedPeopleService nominatedPeopleService;

    @Autowired
    public NominatedPeopleResource(NominatedPeopleService nominatedPeopleService) {
        this.nominatedPeopleService = nominatedPeopleService;

    }

    @GetMapping(value = "/nominatedpeople/{teamUUID}", produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<GetNominatedPeopleResponse> getNominatedPerson(@PathVariable UUID teamUUID) {

            Set<NominatedPerson> nominatedPeople = nominatedPeopleService.getNominatedPerson(teamUUID);
            return ResponseEntity.ok(GetNominatedPeopleResponse.from(nominatedPeople));

    }
}
